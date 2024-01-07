import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthenticationResponse } from '../../models/authentication-response';

@Injectable({
  providedIn: 'root'
})
export class HttpUserTokenService implements HttpInterceptor{

  constructor() { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
    const authResponse: AuthenticationResponse = JSON.parse(storedUser);
    const token = authResponse.token;
    if (token) {
      const authRequest = req.clone({
        headers: new HttpHeaders({
          Authorization: 'Bearer ' + token
        })
      });
      return next.handle(authRequest);
    }
    }
    return next.handle(req);
  }
}
