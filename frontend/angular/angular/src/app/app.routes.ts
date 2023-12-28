import { Routes } from '@angular/router';
import { CustomerComponent } from './components/customer/customer.component';
import { DemoComponent } from './components/demo/demo.component';

export const routes: Routes = [
    {
        path: '',
        component: CustomerComponent
    },
    {
        path: 'demo',
        component: DemoComponent
    }

];
