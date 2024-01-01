import { Component, OnInit } from '@angular/core';
import { MenuBarComponent } from '../menu-bar/menu-bar.component';
import { HeaderBarComponent } from '../header-bar/header-bar.component';
import { ButtonModule } from 'primeng/button';
import { SidebarModule } from 'primeng/sidebar';
import { ManageCustomerComponent } from '../manage-customer/manage-customer.component';
import { CustomerDTO } from '../../models/customer-dto';
import { CustomerServiceService } from '../../services/customer/customer-service.service';
import { CustomerCardsComponent } from '../customer-cards/customer-cards.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [MenuBarComponent,
            HeaderBarComponent,
            ButtonModule,
            SidebarModule,
            ManageCustomerComponent,
            CustomerCardsComponent,
            CommonModule],
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.scss'
})
export class CustomerComponent implements OnInit{
  sidebarVisible2: boolean = false;
  customers: Array<CustomerDTO> = [];
  constructor( private customerService: CustomerServiceService){

  }

  // when initialize the component will fetch all the customers
  ngOnInit(): void {
    this.findAllCustomers();
  }

  private findAllCustomers() {
    this.customerService.findAll()
    .subscribe({
      next: (data: CustomerDTO[]) => {
        this.customers = data;
      }
    });
  }
}
