import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Project} from '../classes/Project';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private apiUrl = 'http://localhost:8082/ProjectController';

  constructor(private http: HttpClient) { }


  getAllProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.apiUrl);
  }


  getProjectById(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.apiUrl}/${id}`);
  }


  getProjectByKey(projectKey: string): Observable<Project> {
    return this.http.get<Project>(`${this.apiUrl}/key/${projectKey}`);
  }

  searchProjectsByName(searchTerm: string): Observable<Project[]> {
    const params = new HttpParams().set('term', searchTerm);
    return this.http.get<Project[]>(`${this.apiUrl}/search`, { params });
  }

  /**
   * Recherche avancée (nom, clé, type)
   */
  advancedSearch(searchTerm: string): Observable<Project[]> {
    const params = new HttpParams().set('term', searchTerm);
    return this.http.get<Project[]>(`${this.apiUrl}/advanced-search`, { params });
  }


  /**
   * Déclenche une synchronisation des projets depuis Jira
   * et retourne le nombre de nouveaux projets ajoutés
   */
  syncProjectsFromJira(): Observable<number> {
    return this.http.post<number>(`${this.apiUrl}/sync`, {});
  }

}
