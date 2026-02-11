export interface TransactionResponse {
  id: number;
  fromAccountId: string;
  fromAccountHolderName: string;
  toAccountId: string;
  toAccountHolderName: string;
  amount: number;
  status: string;
  failureReason?: string | null;
  category?: string | null;
  note?: string | null;
  createdOn: string;
}
