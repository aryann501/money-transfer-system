import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { TransferService } from '../services/transfer.service';
import { RewardService } from '../services/reward.service';
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
export class TransferComponent implements OnInit {
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

  adjustedAmount: number = 0;
  rawAmount: number = 0;
  originalAmount: number = 0;
  availablePoints: number = 0;

  constructor(private fb: FormBuilder, private transferService: TransferService, private rewardService: RewardService, private router: Router) {
    this.availablePoints = 0; // will be loaded from API
    this.originalAmount = 0;
    this.rawAmount = 0;
    this.form = this.fb.group({
      toAccountId: ['', [Validators.required, this.accountIdValidator]],
      amount: [null, [Validators.required, Validators.min(0.01)]],
      category: ['RENT', [Validators.required]],
      note: [''],
      pointsToUse: [0, [Validators.min(0), this.pointsValidator.bind(this)]]
    });

    // Subscribe to amount changes to keep originalAmount and rawAmount in sync
    this.amount.valueChanges.subscribe((val) => {
      this.originalAmount = val ?? 0;
      this.rawAmount = this.originalAmount;
      this.updateAdjustedAmount();
    });
    // Also react to points changes to recalculate adjusted amount
    this.pointsToUse.valueChanges.subscribe(() => {
      this.updateAdjustedAmount();
    });
  }

  ngOnInit(): void {
    // Fetch available reward points from backend
    this.rewardService.getMyRewards().subscribe((res) => {
      this.availablePoints = (res as any).availablePoints ?? 0;
    }, (error) => {
      console.error('Failed to load reward points', error);
    });
  }

  accountIdValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
      return null;
    }
    const pattern = /^ACC\d{4}$/;
    return pattern.test(control.value) ? null : { invalidAccountId: true };
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  get toAccountId() { return this.form.get('toAccountId')!; }
  get amount() { return this.form.get('amount')!; }
  get category() { return this.form.get('category')!; }
  get note() { return this.form.get('note')!; }
  get pointsToUse() { return this.form.get('pointsToUse')!; }

  pointsValidator(control: AbstractControl): ValidationErrors | null {
    const pts = control.value ?? 0;
    const amt = this.originalAmount ?? 0;
    if (pts > this.availablePoints) {
      return { exceedsAvailable: true };
    }
    if (pts > amt) {
      return { exceedsAmount: true };
    }
    return null;
  }

  private updateAdjustedAmount(): void {
    const pts = this.pointsToUse.value ?? 0;
    this.adjustedAmount = Math.max(0, this.originalAmount - pts);
    this.amount.setValue(this.adjustedAmount, { emitEvent: false });
  }

  private generateIdempotencyKey(): string {
    return 'tx-' + Date.now() + '-' + Math.random().toString(16).slice(2);
  }

  onSubmit(): void {
    if (this.form.invalid || this.submitting) {
      this.form.markAllAsTouched();
      return;
    }
    // Ensure latest points are reflected before submission
    this.updateAdjustedAmount();

    this.submitting = true;
    this.serverErrorMessage = null;
    this.successMessage = null;

    const body = {
      toAccountId: this.toAccountId.value,
      amount: this.rawAmount,
      category: this.category.value,
      note: this.note.value,
      pointsToUse: this.pointsToUse.value,
      idempotencyKey: this.generateIdempotencyKey(),
    };

    this.transferService.transferAsUser(body).subscribe({
      next: (res) => {
        this.submitting = false;
        this.receiptData = res;
        this.showReceiptWithCountdown(10);
        this.form.reset({ toAccountId: '', amount: null, category: 'RENT', note: '', pointsToUse: 0 });
        this.rawAmount = 0;
        this.adjustedAmount = 0;
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
        this.router.navigate(['/transfer']);
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
  }

  isControlInvalid(controlName: string): boolean {
    const ctrl = this.form.get(controlName);
    return !!ctrl && ctrl.invalid && (ctrl.touched || ctrl.dirty);
  }

  isSubmitDisabled(): boolean {
    return this.form.invalid || this.submitting;
  }
}