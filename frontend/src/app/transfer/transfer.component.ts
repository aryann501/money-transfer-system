import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { TransferService } from '../services/transfer.service';
import { TransactionResponse } from '../models/transaction-response.model';
import { Router } from '@angular/router';

interface UiCategoryOption {
  value: string;
  label: string;
}

@Component({
  selector: 'app-transfer-money',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.css'],
})
export class TransferComponent {
  form: FormGroup;
  submitting = false;
  serverErrorMessage: string | null = null;
  successMessage: string | null = null;

  receiptVisible = false;
  receiptData: TransactionResponse | null = null;
  countdownSeconds = 0;
  private countdownTimer: any;

  categories: UiCategoryOption[] = [
    { value: 'GROCERY', label: 'Grocery' },
    { value: 'STATIONERY', label: 'Stationery' },
    { value: 'RENT', label: 'Rent' },
    { value: 'SALARY', label: 'Salary' },
    { value: 'UTILITIES', label: 'Utilities' },
    { value: 'ENTERTAINMENT', label: 'Entertainment' },
    { value: 'OTHER', label: 'Other' },
  ];

  constructor(private fb: FormBuilder, private transferService: TransferService, private router: Router) {
    this.form = this.fb.group({
      toAccountId: ['', [Validators.required]],
      amount: [null, [Validators.required, Validators.min(1)]],
      category: ['RENT', [Validators.required]],
      note: [''],
    });
  }

  
    goBack(): void{
      this.router.navigate(['/dashboard']);
    }

  get toAccountId() { return this.form.get('toAccountId')!; }
  get amount() { return this.form.get('amount')!; }
  get category() { return this.form.get('category')!; }
  get note() { return this.form.get('note')!; }

  private generateIdempotencyKey(): string {
    return 'tx-' + Date.now() + '-' + Math.random().toString(16).slice(2);
  }

  onSubmit(): void {
    if (this.form.invalid || this.submitting) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.serverErrorMessage = null;
    this.successMessage = null;

    const body = {
      toAccountId: this.toAccountId.value,
      amount: this.amount.value,
      category: this.category.value,
      note: this.note.value,
      idempotencyKey: this.generateIdempotencyKey(),
    };

    this.transferService.transferAsUser(body).subscribe({
      next: (res) => {
        this.submitting = false;
        this.receiptData = res;
        this.showReceiptWithCountdown(10);
        this.form.reset({ toAccountId: '', amount: null, category: 'RENT', note: '' });
      },
      error: (err: HttpErrorResponse) => {
        this.submitting = false;
        this.handleServerError(err);
      },
    });
  }

  private showReceiptWithCountdown(seconds: number): void {
    this.receiptVisible = true;
    this.countdownSeconds = seconds;
    if (this.countdownTimer) clearInterval(this.countdownTimer);

    this.countdownTimer = setInterval(() => {
      this.countdownSeconds -= 1;
      if (this.countdownSeconds <= 0) {
        clearInterval(this.countdownTimer);
        this.receiptVisible = false;
        this.receiptData = null;
      }
    }, 1000);
  }

  private handleServerError(err: HttpErrorResponse): void {
    let message: string;

    if (typeof err.error === 'string') {
      // backend returned plain string
      message = err.error;
    } else if (err.error && (err.error.message || err.error.failureReason)) {
      // backend returned JSON object
      message = err.error.message || err.error.failureReason;
    } else {
      message = err.message || 'Transaction failed';
    }

    this.serverErrorMessage = message;
    const lower = message.toLowerCase();

    if (lower.includes('account not found')) {
      this.toAccountId.setErrors({ ...(this.toAccountId.errors || {}), server: true });
    }
    if (lower.includes('insufficient balance')) {
      this.amount.setErrors({ ...(this.amount.errors || {}), server: true });
    }
    if (lower.includes('duplicate transfer')) {
      this.amount.setErrors({ ...(this.amount.errors || {}), server: true });
      this.toAccountId.setErrors({ ...(this.toAccountId.errors || {}), server: true });
    }
  }

  isControlInvalid(controlName: string): boolean {
    const ctrl = this.form.get(controlName);
    return !!ctrl && ctrl.invalid && (ctrl.touched || ctrl.dirty);
  }

  hasServerError(controlName: string): boolean {
    const ctrl = this.form.get(controlName);
    return !!ctrl && !!ctrl.errors?.['server'];
  }

  isSubmitDisabled(): boolean {
    return this.form.invalid || this.submitting;
  }
}

