import {Component, Inject, OnInit} from '@angular/core';
import {Subject} from 'rxjs';
import {AuthServiceService} from '../../services/auth-service.service';
import {Router} from '@angular/router';
import {User} from '../../classes/User';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})


export class LoginComponent implements OnInit{

  email: string = '';
  password: string = '';
  userRole!: string;
  errorMessage: string = '';

  constructor(
    private authService: AuthServiceService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Si déjà connecté, rediriger automatiquement
    if (localStorage.getItem('isLoggedIn') === 'true') {
      this.router.navigate(['/dashboard']);
    }
  }
  /*normalLogin(): void {
    this.authService.login(this.email, this.password).subscribe(
      (response: any) => {
        console.log(response); // affichera "Login successful as USER" ou autre
        if (response.includes('Login successful as')) {
          const role = response.split('Login successful as ')[1];
          this.userRole = role.trim();

          window.localStorage.setItem('role', this.userRole);
          window.localStorage.setItem('email', this.email);


          window.localStorage.setItem('isLoggedIn', 'true');
          this.redirectUser();
        } else {
          this.errorMessage = 'Invalid email or password';
        }
      },
      (error) => {
        console.error('Login error:', error);
        this.errorMessage = 'Login failed, please try again.';
      }
    );
  }*/
  normalLogin(): void {
    this.authService.login(this.email, this.password).subscribe(
      (user: User) => {
        console.log("✅ User connecté :", user);

        // Stocker toutes les informations dans localStorage
        localStorage.setItem('firstName', user.firstName);
        localStorage.setItem('lastName', user.lastName);
        localStorage.setItem('email', user.email);
        localStorage.setItem('role', user.role);
        localStorage.setItem('businessUnit', user.businessUnit);
        localStorage.setItem('service', user.service);
        localStorage.setItem('responsable', user.responsable);
        localStorage.setItem('isLoggedIn', 'true');

        // Réinitialiser le formulaire
        this.email = '';
        this.password = '';

        this.redirectUser();
      },
      (error) => {
        console.error('Login error:', error);
        this.errorMessage = 'Invalid email or password';
      }
    );
  }
  goToSignup(): void {
    this.router.navigate(['/signup']);
  }

  private redirectUser(): void {

      this.router.navigate(['/dashboard']).then(() => window.location.reload());

  }
}
