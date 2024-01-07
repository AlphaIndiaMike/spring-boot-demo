import { Injectable, Inject, PLATFORM_ID  } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthenticationResponse } from '../../models/authentication-response';
import { JwtHelperService } from '@auth0/angular-jwt';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AccessGuardService implements CanActivate {

  constructor(private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object) { }


  canActivate(route: ActivatedRouteSnapshot, 
              state: RouterStateSnapshot
  ): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> 
  {
    if (isPlatformBrowser(this.platformId)) {
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
    }
    this.router.navigate(['login']);
    return false;
  }
}
