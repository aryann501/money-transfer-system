import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'transfer', loadComponent: () => import('./transfer/transfer.component').then(m => m.TransferComponent), canActivate: [AuthGuard] },
  { path: 'history', loadComponent: () => import('./history/history.component').then(m => m.HistoryComponent), canActivate: [AuthGuard] },
  { path: 'details', loadComponent: () => import('./details/details.component').then(m => m.DetailsComponent), canActivate: [AuthGuard] },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];
