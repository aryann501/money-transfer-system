export interface TransactionResponse {
    fromAccountId: string;
    fromAccountHolderName: string;
    toAccountId: string;
    toAccountHolderName: string;
    amount: number;
    status: string;
    failureReason?: string;
    category?: string;
    note?: string;
    createdOn: string;
}