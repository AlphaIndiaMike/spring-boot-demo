import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthenticationRequest } from '../../models/authentication-request';
import { Observable } from 'rxjs';
import { AuthenticationResponse } from '../../models/authentication-response';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(
    private http: HttpClient
  ) { }

  login(authReq: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>('http://localhost:8080/api/v1/auth/login', authReq);
  }
}
