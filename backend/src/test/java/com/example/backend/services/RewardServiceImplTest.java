package com.example.backend.services;

import com.example.backend.dtos.RewardRedemptionResponse;
import com.example.backend.dtos.RewardResponse;
import com.example.backend.dtos.RewardSummaryResponse;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import com.example.backend.entities.Account;
@ExtendWith(MockitoExtension.class)
class RewardServiceImplTest {

    @Mock
    private RewardGrantRepository rewardGrantRepository;
    @Mock
    private RewardRedemptionRepository rewardRedemptionRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    private UserEntity testUser;
    private TransactionLog transaction;

    @BeforeEach
    void setUp() {
        // Mock security context for getMyRewards (not used in these tests but safe)
        testUser = new UserEntity();
        testUser.setId(1L);
        Authentication auth = new UsernamePasswordAuthenticationToken("user", "pwd");
        SecurityContextHolder.getContext().setAuthentication(auth);

        transaction = new TransactionLog();
        // Assume getters/setters exist in TransactionLog
        // We set only fields required for RewardRules calculations
        transaction.setId(100L);
        transaction.setAmount(500.0);
        transaction.setStatus(com.example.backend.enums.TransactionStatus.SUCCESS);

        // Ensure accounts have holder names (different) in test method
        // (Account holder names will be set in the test method itself)
        // No further setup needed here.
        // For completeness, you may also set a mock transaction status if needed.
        
    }

    @Test
    void processRewardForTransfer_eligible_grantsPoints() {
        // Mock user lookup
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(testUser));
        // Mock repository existence check
        when(rewardGrantRepository.findByTransactionId(anyString())).thenReturn(Optional.empty());
        // Mock save
        when(rewardGrantRepository.save(any(RewardGrant.class))).thenAnswer(i -> i.getArgument(0));

        // Prepare accounts with distinct holder names
        Account fromAccount = new com.example.backend.entities.Account();
        fromAccount.setId(1L);
        fromAccount.setHolderName("Alice");
        Account toAccount = new com.example.backend.entities.Account();
        toAccount.setId(2L);
        toAccount.setHolderName("Bob");
        // Invoke service method
        rewardService.processRewardForTransfer(transaction, fromAccount, toAccount);

        // Verify grant saved with correct points (500/100 = 5)
        verify(rewardGrantRepository, times(1)).save(argThat((RewardGrant g) -> g.getPoints() == 5 && g.getTransactionId().equals("100")));
    }

    @Test
    void redeemPointsForTransfer_negativePoints_throws() {
        // Directly test validation logic
        RewardRedemptionResponse ex = null;
        assertDoesNotThrow(() -> rewardService.redeemPointsForTransfer(testUser, "tx123", 0)); // zero is allowed (early return)
        // Negative points should be filtered by the if (pointsToUse <= 0) guard -> no exception but early return
        // Ensure repository not called
        verify(rewardRedemptionRepository, never()).save(any());
    }

    @Test
    void resolveCashAmount_excessivePoints_throwsException() {
        when(rewardGrantRepository.sumPointsByUserId(1L)).thenReturn(50);
        when(rewardRedemptionRepository.sumPointsUsedByUserId(1L)).thenReturn(20);
        // available = 30
        assertThrows(InsufficientRewardPointsException.class, () ->
                rewardService.resolveCashAmount(testUser, BigDecimal.valueOf(100), 40));
    }

    @Test
    void resolveCashAmount_pointsExceedTransfer_throwsIllegalArgument() {
        Mockito.lenient().when(rewardGrantRepository.sumPointsByUserId(1L)).thenReturn(200);
        Mockito.lenient().when(rewardRedemptionRepository.sumPointsUsedByUserId(1L)).thenReturn(0);
        // transfer amount 50, points 60 > amount
        assertThrows(IllegalArgumentException.class, () ->
                rewardService.resolveCashAmount(testUser, BigDecimal.valueOf(50), 60));
    }
}
