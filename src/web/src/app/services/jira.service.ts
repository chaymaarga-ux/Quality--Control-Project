
import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Task} from '../classes/Task';
//import {ProjectJiraDto} from '../classes/Project';

@Injectable({
  providedIn: 'root'
})
export class JiraService {

  private apiUrl = 'http://localhost:8082/jira';

  constructor(private http: HttpClient) { }
  getFilteredIssuesByCode(code: string): Observable<{ [projectKey: string]: Task[] }> {
    return this.http.get<{ [projectKey: string]: Task[] }>(`${this.apiUrl}/filter/${code}`);
  }

  getGroupedFilteredResults(): Observable<{
    [projectKey: string]: {
      [filterCode: string]: any[];
    };
  }> {
    return this.http.get<{
      [projectKey: string]: {
        [filterCode: string]: any[];
      };
    }>(`${this.apiUrl}/grouped`);
  }

  /*
  // Récupérer tous les projets directement depuis Jira
  getAllProjects(): Observable<ProjectJiraDto[]> {
    return this.http.get<ProjectJiraDto[]>(`${this.apiUrl}/projects`);
  }

 /!* // Récupérer toutes les issues d'un projet
  getIssuesForProject(projectKey: string): Observable<{ issues: IssueDto[] }> {
    return this.http.get<{ issues: IssueDto[] }>(`${this.apiUrl}/projects/${projectKey}/issues`);
  }*!/

  /!*!// Récupérer les tâches principales d'un projet
  getMainTasks(projectKey: string): Observable<{ issues: IssueDto[] }> {
    return this.http.get<{ issues: IssueDto[] }>(`${this.apiUrl}/projects/${projectKey}/main-tasks`);
  }*!/

 /!* // Récupérer les sous-tâches d'un projet
  getSubTasksByProject(projectKey: string): Observable<{ issues: IssueDto[] }> {
    return this.http.get<{ issues: IssueDto[] }>(`${this.apiUrl}/projects/${projectKey}/subtasks`);
  }*!/

 /!* // Récupérer les sous-tâches d'une tâche spécifique
  getSubTasks(parentKey: string): Observable<{ issues: IssueDto[] }> {
    return this.http.get<{ issues: IssueDto[] }>(`${this.apiUrl}/tasks/${parentKey}/subtasks`);
  }*!/
  */
}

