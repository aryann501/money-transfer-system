export interface RewardResponse {
  id: string;
  transactionId: string;
  points: number;
  value: number;
}

export interface RewardSummaryResponse {
  /** Spendable balance (earned minus redeemed). 1 point = ₹1. */
  availablePoints: number;
  totalEarned: number;
  totalRedeemed: number;
  history: RewardResponse[];
}
