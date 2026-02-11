import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { SignupRequest } from '../models/signup-request.model';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink], 
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  signupForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.signupForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      holderName: ['', Validators.required],
      minBalance: [1000, [Validators.required, Validators.min(1000)]]
    });
  }

  onSubmit(): void {
    if (this.signupForm.valid) {
      const request: SignupRequest = this.signupForm.value;

      this.authService.signup(request).subscribe({
        next: (response) => {
          alert(`Signup successful! Account ID: ${response.accountId}`);
          this.router.navigate(['/login']);   // ✅ redirect to login
        },
        error: (err) => {
          console.error('Signup failed', err);
          if (err.status === 400 && err.error?.message?.includes('username')) {
            alert('Username already taken. Please choose another.');
          } else {
            alert('Signup failed. Please try again.');
          }
        }
      });
    } else {
      alert('Please fill all fields correctly.');
    }
  }
}
