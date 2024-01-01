import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthenticationResponse } from '../../models/authentication-response';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AccessGuardService implements CanActivate {

  constructor(private router: Router) { }
  canActivate(route: ActivatedRouteSnapshot, 
              state: RouterStateSnapshot
  ): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> 
  {
     const storedUser = localStorage.getItem('user');
     if (storedUser) {
      const authResponse: AuthenticationResponse = JSON.parse(storedUser);
      const token = authResponse.token;
      if (token) {
        const jwtHelper = new JwtHelperService;
        const isTokenNotExpired = !jwtHelper.isTokenExpired(token);
        if (isTokenNotExpired) return true;
      }
     }

     this.router.navigate(['login']);
     return false;
  }
}
