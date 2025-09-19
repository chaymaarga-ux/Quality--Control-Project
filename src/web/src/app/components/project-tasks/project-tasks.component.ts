import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TaskService} from '../../services/task.service';
import {ProjectService} from '../../services/project.service';
import {Project} from '../../classes/Project';
import {Task} from '../../classes/Task';



@Component({
  selector: 'app-project-tasks',
  standalone: false,
  templateUrl: './project-tasks.component.html',
  styleUrl: './project-tasks.component.css'
})
export class ProjectTasksComponent implements OnInit {
  projectId!: number;
  projectName!: string;
  tasks: any[] = [];
  isLoading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private taskService: TaskService,
    private projectService: ProjectService,
    private router: Router
  ) {}

  ngOnInit() {
    this.projectId = +this.route.snapshot.paramMap.get('projectId')!;
    console.log('Project ID:', this.projectId);
    this.loadProjectTasks();
    this.loadProjectName();
  }

  loadProjectName() {
    this.projectService.getProjectById(this.projectId).subscribe(
      project => this.projectName = project.name
    );
  }
  loadProjectTasks() {
    this.isLoading = true;
    this.taskService.getTasksByProject(this.projectId).subscribe(
      (data) => {
        this.tasks = data;
        this.tasks = data.filter(task =>
          task.issueTypeName?.toLowerCase() !== 'non operative'
        );
        this.isLoading = false;
      },
      (error) => {
        this.error = 'Erreur lors du chargement des tâches';
        this.isLoading = false;
        console.error(error);
      }
    );
  }

  // Méthode pour synchroniser les tasks depuis Jira
  syncTasksFromJira(): void {
    this.taskService.syncTasksFromJira().subscribe({
      next: (count) => {
        console.log(`${count} nouveaux tasks ajoutés`);
        alert(`Synchronisation réussie ! ${count} nouveaux tasks ajoutés.`);
        this.loadProjectTasks();
      },
      error: (err) => {
        console.error('Erreur de synchronisation :', err);
        alert(`Erreur lors de la synchronisation : ${err.message}`);
      }
    });
  }

  navigateToSubTasks(task: Task) {
    this.router.navigate(['/subtasks', task.taskId]);
  }

}
