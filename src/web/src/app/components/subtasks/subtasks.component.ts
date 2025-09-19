import {Component, OnInit} from '@angular/core';
import {Project} from '../../classes/Project';
import {ActivatedRoute, Router} from '@angular/router';
import {TaskService} from '../../services/task.service';
import {ProjectService} from '../../services/project.service';
import {SubtaskService} from '../../services/subtask.service';

@Component({
  selector: 'app-subtasks',
  standalone: false,
  templateUrl: './subtasks.component.html',
  styleUrl: './subtasks.component.css'
})
export class SubtasksComponent  implements OnInit {
  taskId!: number;
  taskName!: string;
  tasks: any[] = [];
  isLoading = true;
  error: string | null = null;


  constructor(
    private route: ActivatedRoute,
    private subtaskService: SubtaskService,
    private taskService: TaskService,
    private router: Router
  ) {}

  ngOnInit() {
    this.taskId = +this.route.snapshot.paramMap.get('taskId')!;
    console.log('task ID:', this.taskId);
    this.loadProjectSubTasks();
    this.loadTaskName();
  }

  loadTaskName() {
    this.taskService.getTaskById(this.taskId).subscribe(
      task => this.taskName = task.taskKey
    );
  }
  loadProjectSubTasks() {
    this.isLoading = true;
    this.subtaskService.getSubTasksByTask(this.taskId).subscribe(
      (data) => {
        console.log('Données reçues depuis l\'API :', data);
        this.tasks = data;
        this.tasks = data.filter(task =>
          task.issueTypeName?.toLowerCase() !== 'non operative'
        );
        this.isLoading = false;
      },
      (error) => {
        this.error = 'Erreur lors du chargement des sous-tâches';
        this.isLoading = false;
        console.error(error);
      }
    );
  }

}
