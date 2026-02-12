import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { LoginRequest } from '../models/login-request.model';
import { JwtResponse } from '../models/jwt-response.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink, RouterOutlet],   
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  errorMessage: string | null = null;   // ✅ inline error message

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // No auto-redirect here — user can still open login page
  }

  onSubmit(): void {
    this.errorMessage = null; // reset before each submit

    const existingUser = localStorage.getItem('user');
    if (existingUser) {
      // ✅ Block login if *any* user is already logged in
      this.errorMessage = 'A user is already logged in. Please log out first.';
      return;
    }

    if (this.loginForm.valid) {
      const request: LoginRequest = this.loginForm.value;

      this.authService.login(request).subscribe({
        next: (response: JwtResponse) => {
          // Save token
          this.authService.saveToken(response.accessToken);

          // Save user info in localStorage for dashboard
          localStorage.setItem('user', JSON.stringify(response));

          // Navigate to dashboard
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.error('Login failed', err);

          if (typeof err.error === 'string') {
            this.errorMessage = err.error;   // plain string from GlobalExceptionHandler
          } else if (err.error?.message) {
            this.errorMessage = err.error.message; // JSON from AuthEntryPointJwt
          } else {
            this.errorMessage = 'Login failed. Please try again.';
          }
        }
      });
    } else {
      this.errorMessage = 'Please enter both username and password.';
    }
  }
}
