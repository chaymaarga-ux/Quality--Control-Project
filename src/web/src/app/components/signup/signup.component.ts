import { Component } from '@angular/core';
import {AuthServiceService} from '../../services/auth-service.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-signup',
  standalone: false,
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  firstName = '';
  lastName = '';
  email = '';
  password = '';
  businessUnit = '';
  service = '';
  responsable = '';
  role = ''; // si souhaité, sinon laissé vide

  errorMessage = '';
  successMessage = '';

  constructor(private authService: AuthServiceService, private router: Router) {}

  signup(): void {
    if (
      !this.firstName ||
      !this.lastName ||
      !this.email ||
      !this.password ||
      !this.businessUnit ||
      !this.service ||
      !this.responsable
    ) {
      this.errorMessage = 'Please fill in all required fields.';
      this.successMessage = '';
      return; // stop execution if a required field is missing
    }
    const user = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      password: this.password,
      businessUnit: this.businessUnit,
      service: this.service,
      responsable: this.responsable,
      role: this.role
    };

    this.authService.signup(user).subscribe({
      next: (response) => {
        this.successMessage = 'User registered successfully.';
        this.errorMessage = '';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1000);
      },
      error: (error) => {
        this.errorMessage = 'Erreur : ' + error.error;
        this.successMessage = '';
      }
    });
  }
}
