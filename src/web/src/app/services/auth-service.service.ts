import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../classes/User';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

  private apiUrl = 'http://localhost:8082/api/auth';

  constructor(private http: HttpClient) { }

/*  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { email, password }, { responseType: 'text' });
  }*/

  login(email: string, password: string): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/login`, { email, password });
  }
  signup(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/signup`, user, { responseType: 'text' });
  }

}
