import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthenticationRequest } from '../../models/authentication-request';
import { Observable } from 'rxjs';
import { AuthenticationResponse } from '../../models/authentication-response';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private readonly authUrl = `${environment.api.baseUrl}${environment.api.authUrl}`;

  constructor(
    private http: HttpClient
  ) { }

  login(authReq: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(this.authUrl, authReq);
  }
}
