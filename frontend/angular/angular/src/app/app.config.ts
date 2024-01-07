import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { HttpUserTokenService } from './services/userToken/http-user-token.service';

export const appConfig: ApplicationConfig = {
  providers: [provideAnimations(),
    provideRouter(routes), 
    provideClientHydration(), 
    importProvidersFrom(HttpClientModule),
    provideHttpClient(withFetch()),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpUserTokenService,
      multi: true
    }
  ]
};
