import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RewardSummaryResponse } from '../models/reward-summary-response.model';

@Injectable({
  providedIn: 'root'
})
export class RewardService {
  private baseUrl = 'http://localhost:8080/api/rewards';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  /**
   * Fetch the reward summary for the currently authenticated user.
   */
  getMyRewards(): Observable<RewardSummaryResponse> {
    return this.http.get<RewardSummaryResponse>(
      `${this.baseUrl}/me`,
      { headers: this.getAuthHeaders() }
    );
  }
}
