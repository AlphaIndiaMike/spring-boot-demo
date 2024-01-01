import { Routes } from '@angular/router';
import { CustomerComponent } from './components/customer/customer.component';
import { DemoComponent } from './components/demo/demo.component';
import { LoginComponent } from './components/login/login.component';
import { AccessGuardService } from './services/urlGuard/access-guard.service';

export const routes: Routes = [
    {
        path: 'customers',
        component: CustomerComponent,
        canActivate: [AccessGuardService]
    },
    {
        path: 'demo',
        component: DemoComponent
    },
    {
        path: 'login',
        component: LoginComponent
    },

];
