import { Component } from '@angular/core';
import { MenuBarComponent } from '../menu-bar/menu-bar.component';
import { HeaderBarComponent } from '../header-bar/header-bar.component';

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [MenuBarComponent,
            HeaderBarComponent],
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.scss'
})
export class CustomerComponent {

}
