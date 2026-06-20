import { Component, OnInit } from '@angular/core';
import { RewardService } from '../services/reward.service';
import { RewardSummaryResponse, RewardResponse } from '../models/reward-summary-response.model';
import { DatePipe, DecimalPipe, NgClass, NgFor, NgIf } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reward-history',
  standalone: true,
  imports: [DatePipe, NgIf, NgFor, NgClass, DecimalPipe],
  templateUrl: './reward-history.component.html',
  styleUrls: ['./reward-history.component.css']
})
export class RewardHistoryComponent implements OnInit {
  rewardSummary: RewardSummaryResponse | null = null;
  // Tabs: incoming (+), outgoing (-), and all history
  activeTab: 'incoming' | 'outgoing' | 'all' = 'all';

  constructor(private rewardService: RewardService, private router: Router) { }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  ngOnInit(): void {
    this.rewardService.getMyRewards().subscribe({
      next: (res) => this.rewardSummary = res,
      error: (err) => console.error('Failed to fetch reward summary', err)
    });
  }

  /**
   * Returns the reward history filtered according to the selected tab.
   */
  get filteredHistory() {
    if (!this.rewardSummary) return [];
    switch (this.activeTab) {
      case 'incoming':
        return this.rewardSummary.history.filter(r => r.points > 0);
      case 'outgoing':
        return this.rewardSummary.history.filter(r => r.points < 0);
      case 'all':
        return this.rewardSummary.history;
      default:
        return [];
    }
  }

  setTab(tab: 'incoming' | 'outgoing' | 'all'): void {
    this.activeTab = tab;
  }
}
