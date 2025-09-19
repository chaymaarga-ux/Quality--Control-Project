import {Component, OnInit} from '@angular/core';
import {Project} from '../../classes/Project';
import {ProjectService} from '../../services/project.service';
import {Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import { MessagesModule } from 'primeng/messages';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@Component({
  selector: 'app-project-list',
  standalone: false,
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.css'
})
export class ProjectListComponent implements OnInit {
  projects: Project[] = [];
  isLoading = false;
  error: string | null = null;
  searchKeyword: string = '';
  filteredProjects: Project[] = [];


  constructor(private projectService: ProjectService ,  private router: Router , public messageService: MessageService  ) { }

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.isLoading = true;
    this.projectService.getAllProjects().subscribe({
      next: (data) => {
        this.projects = data;
        this.filteredProjects = [...data]; // Copie initiale pour la recherche
        this.isLoading = false;
      },
      error: (err) => {
        this.error = `Erreur lors du chargement des projets: ${err.message}`;
        this.isLoading = false;
      }
    });
  }


  getTypeIconClass(type: string | undefined): string {
    if (!type) return 'business';

    type = type.toLowerCase();

    if (type.includes('business') || type.includes('company')) {
      return 'business';
    } else if (type.includes('software') || type.includes('it')) {
      return 'software';
    } else if (type.includes('service') || type.includes('support')) {
      return 'service';
    }

    return 'business'; // Type par défaut
  }

  getTypeClass(type: string | undefined): string {
    if (!type) return 'type-business';

    type = type.toLowerCase();

    if (type.includes('business') || type.includes('company')) {
      return 'type-business';
    } else if (type.includes('software') || type.includes('it')) {
      return 'type-software';
    } else if (type.includes('service') || type.includes('support')) {
      return 'type-service';
    }

    return 'type-business';
  }

  getTypeIcon(type: string | undefined): string {
    if (!type) return 'pi-fire';

    type = type.toLowerCase();

    if (type.includes('business') || type.includes('company')) {
      return 'pi-briefcase';
    } else if (type.includes('software') || type.includes('it')) {
      return 'pi-desktop';
    } else if (type.includes('service') || type.includes('support')) {
      return 'pi-wrench';
    }

    return 'pi-briefcase';
  }



  filterProjects(): void {
    const term = this.searchKeyword.toLowerCase().trim();

    if (!term) {
      this.filteredProjects = [...this.projects];
      return;
    }

    this.filteredProjects = this.projects.filter(project =>
      (project.name && project.name.toLowerCase().includes(term)) ||
      (project.projectKey && project.projectKey.toLowerCase().includes(term)) ||
      (project.type && project.type.toLowerCase().includes(term)) ||
      (project.lead && project.lead.toLowerCase().includes(term))
    );
  }

// Méthode pour synchroniser les projets depuis Jira
  syncProjectsFromJira(): void {
    this.projectService.syncProjectsFromJira().subscribe({
      next: (count) => {
        console.log(`${count} nouveaux projets ajoutés`);
        this.messageService.add({
          severity: 'success',
          summary: 'Synchronisation réussie',
          detail: `${count} nouveaux projets ajoutés.`
        });
        this.loadProjects();
      },
      error: (err) => {
        console.error('Erreur de synchronisation :', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Erreur de synchronisation',
          detail: err.message
        });
      }
    });
  }

  navigateToProjectTasks(project: Project) {
      this.router.navigate(['/project-tasks', project.projectId]);
      console.log('Navigating to project tasks for project:', project);

  }
}
