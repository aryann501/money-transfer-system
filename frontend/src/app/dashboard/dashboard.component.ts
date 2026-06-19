// File: e:/Intern/money-transfer-system/frontend/src/app/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { AccountService } from '../services/account.service';
import { RewardService } from '../services/reward.service';
import { Account } from '../models/account.model';
import { JwtResponse } from '../models/jwt-response.model';
import { RewardSummaryResponse } from '../models/reward-summary-response.model';
import { CurrencyPipe, NgIf } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NgIf, CurrencyPipe],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  username = '';
  role = '';
  account: Account | null = null;
  reward: RewardSummaryResponse | null = null;   // <-- NEW

  constructor(
    private router: Router,
    private authService: AuthService,
    private accountService: AccountService,
    private rewardService: RewardService          // <-- NEW
  ) { }

  ngOnInit(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const jwtResponse: JwtResponse = JSON.parse(storedUser);
      this.username = jwtResponse.username;
      this.role = jwtResponse.roles[0];

      if (this.role === 'ROLE_USER') {
        // Account details
        this.accountService.getMyDetails().subscribe(acc => this.account = acc);
        // *** Reward summary ***
        this.rewardService.getMyRewards().subscribe(res => this.reward = res);
      }
    }
  }

  logout(): void {
    this.authService.logout();
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }

  goTo(path: string): void {
    this.router.navigate([path]);
  }
}
