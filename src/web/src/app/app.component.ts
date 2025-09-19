import {Component, ElementRef, HostListener, OnInit, Renderer2} from '@angular/core';
import {Router} from '@angular/router';
import {ProjectService} from './services/project.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent  implements OnInit {
  title = 'Gestion des Projets Jira';
  isPopupVisible = false;
/* person: any | undefined;*/
  person = {
    firstName: '',
    lastName: '',
    email: '',
    role: '',
    businessUnit: '',
    service: '',
    responsable: ''
  };

  errorMessage: string | undefined;
  userRole: string = '';

  constructor(
    private renderer: Renderer2,
    private elementRef: ElementRef,
    private router: Router,
    private projectService: ProjectService
  ) {}

  ngOnInit(): void {
    // Simulation de l'utilisateur connecté pour le développement
    // this.simulateUserData();
    this.person.firstName = localStorage.getItem('firstName') || '';
    this.person.lastName = localStorage.getItem('lastName') || '';
    this.person.email = localStorage.getItem('email') || '';
    this.person.role = localStorage.getItem('role') || '';
    this.person.businessUnit = localStorage.getItem('businessUnit') || '';
    this.person.service = localStorage.getItem('service') || '';
    this.person.responsable = localStorage.getItem('responsable') || '';

    // Récupérer le rôle de l'utilisateur depuis localStorage
    this.userRole = localStorage.getItem('role') || '';
  }

  @HostListener('document:click', ['$event'])
  handleOutsideClick(event: MouseEvent) {
    const targetElement = event.target as HTMLElement;

    // Vérifier si le clic est en dehors du popup
    const clickedInsidePopup = this.elementRef.nativeElement.querySelector('.custom-popup')?.contains(targetElement);
    const clickedProfileIcon = this.elementRef.nativeElement.querySelector('.header-profile a')?.contains(targetElement);

    if (!clickedInsidePopup && !clickedProfileIcon && this.isPopupVisible) {
      this.isPopupVisible = false;
    }
  }


  isLoggedIn(): boolean {
    return localStorage.getItem('isLoggedIn') === 'true';
  }
/*  isLoggedIn(): boolean {
    // Pour le développement, vous pouvez toujours retourner true
    // Dans une application réelle, vérifiez si l'utilisateur est connecté
    const token = localStorage.getItem('auth_token');
    return !!token;
    // return true; // Décommentez cette ligne pour toujours simuler un utilisateur connecté
  }*/

  logout(): void {
    localStorage.clear();
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('email');
    localStorage.removeItem('role');

    this.router.navigateByUrl("/login");
  }

  togglePopup(): void {
    this.isPopupVisible = !this.isPopupVisible;
  }

  // Méthode pour synchroniser les projets depuis Jira
  syncProjectsFromJira(): void {
    this.projectService.syncProjectsFromJira().subscribe({
      next: (count) => {
        console.log(`${count} nouveaux projets ajoutés`);
        alert(`Synchronisation réussie ! ${count} nouveaux projets ajoutés.`);
      },
      error: (err) => {
        console.error('Erreur de synchronisation :', err);
        alert(`Erreur lors de la synchronisation : ${err.message}`);
      }
    });
  }

  private simulateUserData(): void {
    this.person = {
      firstName: 'CHAYMAA',
      lastName: 'AIT TARGA',
      email: 'chaymaa.aittarga.st@emeal.nttdata.com',
      role: 'Application developer',
      businessUnit: 'IT',
      service: 'Developement',
      responsable: 'ABDELOUHAB ELKADIRI'
    };
  }

  toggleMenu(): void {
    // Code pour montrer/cacher le menu latéral
    // Par exemple:
    const sideNav = document.querySelector('.deznav');
    if (sideNav) {
      sideNav.classList.toggle('show');
    }
  }
}
