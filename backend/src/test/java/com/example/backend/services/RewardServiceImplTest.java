package com.example.backend.services;

import com.example.backend.dtos.RewardResponse;
import com.example.backend.dtos.RewardSummaryResponse;
import com.example.backend.entities.RewardGrant;
import com.example.backend.entities.TransactionLog;
import com.example.backend.entities.UserEntity;
import com.example.backend.exceptions.InsufficientRewardPointsException;
import com.example.backend.repositories.RewardGrantRepository;
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

@ExtendWith(MockitoExtension.class)
class RewardServiceImplTest {

    @Mock
    private RewardGrantRepository rewardGrantRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    private UserEntity testUser;
    private TransactionLog transaction;

    @BeforeEach
    void setUp() {
        // Mock security context (not used in these tests but safe)
        testUser = new UserEntity();
        testUser.setId(1L);
        Authentication auth = new UsernamePasswordAuthenticationToken("user", "pwd");
        SecurityContextHolder.getContext().setAuthentication(auth);

        transaction = new TransactionLog();
        transaction.setId(100L);
        transaction.setAmount(500.0);
        transaction.setStatus(com.example.backend.enums.TransactionStatus.SUCCESS);
    }

    @Test
    void processRewardForTransfer_eligible_grantsPoints() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(testUser));
        when(rewardGrantRepository.findByTransactionId(anyString())).thenReturn(Optional.empty());
        when(rewardGrantRepository.save(any(RewardGrant.class))).thenAnswer(i -> i.getArgument(0));

        // Prepare accounts with distinct holder names
        com.example.backend.entities.Account fromAccount = new com.example.backend.entities.Account();
        fromAccount.setId(1L);
        fromAccount.setHolderName("Alice");
        com.example.backend.entities.Account toAccount = new com.example.backend.entities.Account();
        toAccount.setId(2L);
        toAccount.setHolderName("Bob");

        rewardService.processRewardForTransfer(transaction, fromAccount, toAccount);

        verify(rewardGrantRepository, times(1)).save(argThat((RewardGrant g) -> g.getPoints() == 5 && g.getTransactionId().equals("100")));
    }

    @Test
    void resolveCashAmount_excessivePoints_throwsException() {
        when(rewardGrantRepository.sumPointsByUserId(1L)).thenReturn(50);
        // Available points = 50 (no redemption repo)
        assertThrows(InsufficientRewardPointsException.class,
                () -> rewardService.resolveCashAmount(testUser, BigDecimal.valueOf(100), 60));
    }

    @Test
    void resolveCashAmount_pointsExceedTransfer_throwsIllegalArgument() {

        // points > transfer amount
        assertThrows(IllegalArgumentException.class,
                () -> rewardService.resolveCashAmount(testUser, BigDecimal.valueOf(50), 60));
    }
}
