import { Routes } from '@angular/router';
import { CustomerComponent } from './components/customer/customer.component';
import { DemoComponent } from './components/demo/demo.component';
import { LoginComponent } from './components/login/login.component';
import { AccessGuardService } from './services/urlGuard/access-guard.service';
import { RegisterCustomerComponent } from './components/register-customer/register-customer.component';

export const routes: Routes = [
    {
        path: '',
        component: CustomerComponent,
        canActivate: [AccessGuardService]
    },
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
    {
        path: 'register',
        component: RegisterCustomerComponent
    }

];
