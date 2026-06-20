import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Account } from '../models/account.model';
import { RewardService } from '../services/reward.service';
import { RewardSummaryResponse } from '../models/reward-summary-response.model';
import { AccountService } from '../services/account.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {
  account: Account | null = null;
  rewardSummary: RewardSummaryResponse | null = null;
  loading = true;
  error: string | null = null;

  constructor(private accountService: AccountService, private rewardService: RewardService, private router: Router) {}
  
    goBack(): void{
      this.router.navigate(['/dashboard']);
    }

  ngOnInit(): void {
    // Fetch account details
    this.accountService.getMyDetails().subscribe({
      next: (res) => {
        this.account = res;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load account details';
        this.loading = false;
      }
    });
    // Fetch reward summary
    this.rewardService.getMyRewards().subscribe({
      next: (res) => {
        this.rewardSummary = res;
      },
      error: (err) => {
        console.error('Failed to fetch rewards', err);
      }
    });
  }
}
