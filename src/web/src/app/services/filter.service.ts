import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Filter} from '../classes/Filter';

@Injectable({
  providedIn: 'root'
})
export class FilterService {

  private baseUrl = 'http://localhost:8082/FilterController';

  constructor(private http: HttpClient) {}

  getAllFilters(): Observable<Filter[]> {
    return this.http.get<Filter[]>(this.baseUrl);
  }

  addFilter(filter: Filter): Observable<Filter> {
    return this.http.post<Filter>(this.baseUrl, filter);
  }

  updateFilter(id: number, filter: Filter): Observable<Filter> {
    return this.http.put<Filter>(`${this.baseUrl}/${id}`, filter);
  }

  deleteFilter(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}

