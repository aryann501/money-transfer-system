package com.example.backend.services;

import com.example.backend.dtos.RewardRedemptionResponse;
import com.example.backend.dtos.RewardResponse;
import com.example.backend.dtos.RewardSummaryResponse;
import com.example.backend.entities.Account;
import com.example.backend.entities.RewardGrant;
import com.example.backend.entities.RewardRedemption;
import com.example.backend.entities.TransactionLog;
import com.example.backend.entities.UserEntity;
import com.example.backend.exceptions.InsufficientRewardPointsException;
import com.example.backend.repositories.RewardGrantRepository;
import com.example.backend.repositories.RewardRedemptionRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.util.IdGenerator;
import com.example.backend.util.RewardRules;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.backend.security.service.UserDetailsImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {
    private static final Logger logger = LoggerFactory.getLogger(RewardServiceImpl.class);

    private final RewardGrantRepository rewardGrantRepository;
    private final RewardRedemptionRepository rewardRedemptionRepository;
    private final UserRepository userRepository;


    @Override
    public void processRewardForTransfer(TransactionLog transaction,
                                         Account fromAccount, Account toAccount) {
        if (rewardGrantRepository.findByTransactionId(String.valueOf(transaction.getId())).isPresent()) {
            logger.warn("Reward already processed | txId={}", transaction.getId());
            return;
        }

        boolean eligible = RewardRules.isEligible(transaction, fromAccount, toAccount);
        int points = RewardRules.calculatePoints(BigDecimal.valueOf(transaction.getAmount()), eligible);
        logger.debug("Eligibility: {} | Calculated points: {} for transaction {}", eligible, points, transaction.getId());
        if (points <= 0) {
            logger.info("No reward granted | txId={} | eligible={} | amount={}",
                    transaction.getId(), eligible, transaction.getAmount());
            return;
        }

        // Retrieve the user associated with the source account
        var optionalUser = userRepository.findById(fromAccount.getId());
        if (optionalUser.isEmpty()) {
            logger.warn("User not found for account id {} | txId={}", fromAccount.getId(), transaction.getId());
            return;
        }
        UserEntity user = optionalUser.get();
        logger.debug("Processing reward for transaction ID: {}", transaction.getId());
        RewardGrant grant = new RewardGrant(user, String.valueOf(transaction.getId()), points, BigDecimal.valueOf(transaction.getAmount()));
        grant.setId(IdGenerator.generateRewardId());
        rewardGrantRepository.save(grant);

        logger.info("Reward granted | rewardId={} | userId={} | txId={} | points={}",
                grant.getId(), user.getId(), transaction.getId(), points);
    }

    @Override
    public RewardRedemptionResponse redeemPointsForTransfer(UserEntity user, String transactionId, int pointsToUse) {
        if (pointsToUse <= 0) {
            logger.warn("Attempted to redeem non-positive points | userId={} | txId={} | points={}", user.getId(), transactionId, pointsToUse);
            return null; // or could throw exception, but keep existing behavior
        }
        if (rewardRedemptionRepository.findByTransactionId(transactionId).isPresent()) {
            logger.warn("Redemption already processed | txId={}", transactionId);
            return null;
        }

        RewardRedemption redemption = new RewardRedemption(user, transactionId, pointsToUse);
        redemption.setId(IdGenerator.generateRedemptionId());
        rewardRedemptionRepository.save(redemption);

        logger.info("Reward redeemed | redemptionId={} | userId={} | txId={} | points={}",
                redemption.getId(), user.getId(), transactionId, pointsToUse);
        // Build and return response DTO
        return new RewardRedemptionResponse(
                redemption.getId(),
                redemption.getTransactionId(),
                redemption.getPointsUsed(),
                redemption.getRupeeValue(),
                redemption.getCreatedOn()
        );
    }

    @Override
    public int getAvailablePoints(Long userId) {
        int earned = rewardGrantRepository.sumPointsByUserId(userId);
        int redeemed = rewardRedemptionRepository.sumPointsUsedByUserId(userId);
        return Math.max(0, earned - redeemed);
    }

    @Override
    public BigDecimal resolveCashAmount(UserEntity user, BigDecimal transferAmount, Integer rewardPointsToUse) {
        int points = rewardPointsToUse == null ? 0 : rewardPointsToUse;
        if (points < 0) {
            throw new IllegalArgumentException("Reward points to use cannot be negative");
        }
        if (points == 0) {
            return transferAmount;
        }

        BigDecimal rewardValue = BigDecimal.valueOf(points).setScale(2, java.math.RoundingMode.HALF_UP);
        if (rewardValue.compareTo(transferAmount) > 0) {
            throw new IllegalArgumentException(
                    "Reward points cannot exceed transfer amount (1 point = ₹1)");
        }

        int available = getAvailablePoints(user.getId());
        if (points > available) {
            throw new InsufficientRewardPointsException(points, available);
        }

        return transferAmount.subtract(rewardValue).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    @Override
    public RewardSummaryResponse getMyRewards() {
        // Retrieve current authenticated account (placeholder)
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        // Retrieve grant and redemption histories
        List<RewardResponse> history = rewardGrantRepository
                .findByUser_IdOrderByCreatedOnDesc(userId)
                .stream()
                .map(this::toGrantResponse)
                .toList();

        List<RewardRedemptionResponse> redemptions = rewardRedemptionRepository
                .findByUser_IdOrderByCreatedOnDesc(userId)
                .stream()
                .map(this::toRedemptionResponse)
                .toList();

        // Calculate totals based on the retrieved histories
        int totalEarned = history.stream().mapToInt(RewardResponse::getPoints).sum();
        int totalRedeemed = redemptions.stream().mapToInt(RewardRedemptionResponse::getPointsUsed).sum();
        int available = Math.max(0, totalEarned - totalRedeemed);

        return new RewardSummaryResponse(available, totalEarned, totalRedeemed, history, redemptions);
    }

    private RewardResponse toGrantResponse(RewardGrant grant) {
        return new RewardResponse(
                grant.getId(),
                grant.getTransactionId(),
                grant.getPoints(),
                grant.getTransactionAmount(),
                grant.getCreatedOn());
    }

    private RewardRedemptionResponse toRedemptionResponse(RewardRedemption redemption) {
        return new RewardRedemptionResponse(
                redemption.getId(),
                redemption.getTransactionId(),
                redemption.getPointsUsed(),
                redemption.getRupeeValue(),
                redemption.getCreatedOn());
    }
}
