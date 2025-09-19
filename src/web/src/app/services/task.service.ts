import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Task} from '../classes/Task';


@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private apiUrl = 'http://localhost:8082/TaskController';

  constructor(private http: HttpClient) {
  }


  // Récupérer toutes les tâches
  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.apiUrl);
  }

  // Récupérer une tâche par ID
  getTaskById(id: number): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/${id}`);
  }

  // Récupérer une tâche par clé
  getTaskByKey(taskKey: string): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/key/${taskKey}`);
  }

  // Récupérer les tâches par statut
  getTasksByStatus(status: string): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/status/${status}`);
  }

  // Récupérer les tâches par assigné
  getTasksByAssignee(assignee: string): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/assignee/${assignee}`);
  }

  // Récupérer les tâches par projet
  getTasksByProject(projectId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/project/${projectId}`);
  }

  // Créer une nouvelle tâche
  createTask(task: Task): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, task);
  }

  // Mettre à jour une tâche
  updateTask(id: number, task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${id}`, task);
  }

  // Supprimer une tâche
  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Recherche de tâches par terme
  searchTasks(searchTerm: string): Observable<Task[]> {
    const params = new HttpParams().set('term', searchTerm);
    return this.http.get<Task[]>(`${this.apiUrl}/search`, {params});
  }

  // Synchroniser les tâches depuis Jira
  syncTasksFromJira(): Observable<number> {
    return this.http.post<number>(`${this.apiUrl}/sync`, {});
  }

}
