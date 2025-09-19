import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Task} from '../classes/Task';


@Injectable({
  providedIn: 'root'
})
export class SubtaskService {

  private apiUrl = 'http://localhost:8082/SubTaskController';

  constructor(private http: HttpClient) {
  }

  getSubTasksByTask(taskId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/task/${taskId}`);
  }

  // Synchroniser les sous-tâches depuis Jira
  syncSubTasksFromJira(): Observable<number> {
    return this.http.post<number>(`${this.apiUrl}/sync`, {});
  }
}
