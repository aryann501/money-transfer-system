package com.example.backend.controllers;

import com.example.backend.dtos.RewardRedemptionRequest;
import com.example.backend.dtos.RewardRedemptionResponse;
import com.example.backend.dtos.RewardSummaryResponse;
import com.example.backend.entities.UserEntity;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.service.UserDetailsImpl;
import com.example.backend.services.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
@Tag(name = "Rewards", description = "Reward points and grant history")
@SecurityRequirement(name = "bearerAuth")
public class RewardController {

    private static final Logger logger = LoggerFactory.getLogger(RewardController.class);

    private final RewardService rewardService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get my reward summary",
            description = "Returns total reward points and the history of grants for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rewards retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthenticated")
    })
    public ResponseEntity<RewardSummaryResponse> getMyRewards() {
        logger.info("Fetching reward summary for authenticated user");
        return ResponseEntity.ok(rewardService.getMyRewards());
    }

    @PostMapping("/redeem")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Redeem reward points",
            description = "Redeems reward points for a specific transaction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redemption successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request or business rule violation"),
            @ApiResponse(responseCode = "401", description = "Unauthenticated")
    })
    public ResponseEntity<RewardRedemptionResponse> redeemPoints(@Valid @RequestBody RewardRedemptionRequest request) {
        // Resolve current authenticated user
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));

        // Validate available points
        int available = rewardService.getAvailablePoints(userId);
        if (request.getPointsToUse() > available) {
            logger.warn("Insufficient reward points | userId={} | requested={} | available={}",
                    userId, request.getPointsToUse(), available);
            throw new com.example.backend.exceptions.InsufficientRewardPointsException(request.getPointsToUse(), available);
        }

        logger.info("Redeeming points | userId={} | txId={} | points={}",
                userId, request.getTransactionId(), request.getPointsToUse());
        var response = rewardService.redeemPointsForTransfer(user, request.getTransactionId(), request.getPointsToUse());
        return ResponseEntity.ok(response);
    }
}
