import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet,RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { LoginRequest } from '../models/login-request.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink,RouterOutlet],   
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const request: LoginRequest = this.loginForm.value;

      this.authService.login(request).subscribe({
        next: (response) => {
          this.authService.saveToken(response.accessToken); // ✅ store JWT
          this.router.navigate(['/dashboard']);             // ✅ redirect after login
        },
        error: (err) => {
          console.error('Login failed', err);
          if (err.status === 401) {
            alert('Incorrect username or password.');
          } else {
            alert('Login failed. Please try again.');
          }
        }
      });
    } else {
      alert('Please enter both username and password.');
    }
  }
}
