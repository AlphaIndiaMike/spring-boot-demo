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
import { CustomerRegistrationRequest } from '../../models/customer-registration-request';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [MenuBarComponent,
            HeaderBarComponent,
            ButtonModule,
            SidebarModule,
            ManageCustomerComponent,
            CustomerCardsComponent,
            CommonModule,
            ToastModule,
            ConfirmDialogModule],
  providers: [MessageService,
              ConfirmationService],
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.scss'
})
export class CustomerComponent implements OnInit{
  sidebarVisible2: boolean = false;
  customers: Array<CustomerDTO> = [];
  customer: CustomerRegistrationRequest = {};
  operation: 'create' | 'update' = 'create';
  
  
  constructor( private customerService: CustomerServiceService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService){

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

  customerId?: number = 0;

  save(cust: CustomerRegistrationRequest){
    if (cust) {
      if (this.operation === 'create') {
        this.customerService.registerCustomer(cust)
        .subscribe({
          next: () => {
            this.findAllCustomers();
            this.sidebarVisible2 = false;
            this.customer={};
            this.messageService.add({
              severity:'success',
              summary:'Success',
              detail:'User created!'
            })
          }
        });
      }
      else{
        this.customerService.updateCustomer(this.customerId, cust)
        .subscribe({
          next: () => {
            this.findAllCustomers();
            this.sidebarVisible2 = false;
            this.customer={};
            this.messageService.add({
              severity:'success',
              summary:'Success',
              detail:'User updated!'
            })
          }
        });
      }
    }
  }

  closeDrawer(){
    this.sidebarVisible2 = false;
    this.customer={};
  }

  updateCustomer(customer: CustomerDTO){
    this.sidebarVisible2 = true;
    this.customer = customer;
    this.operation='update';
    this.customerId = customer.id;
  }

  createCustomer(){
    this.sidebarVisible2 = true;
    this.customer = {};
    this.operation='create';
  }

  deleteCustomer(customer: CustomerDTO) {
    this.confirmationService.confirm({
        message: `Do you want to delete ${customer.name}`,
        header: 'Delete Confirmation',
        icon: 'pi pi-info-circle',
        acceptButtonStyleClass:"p-button-danger p-button-text",
        rejectButtonStyleClass:"p-button-text p-button-text",
        acceptIcon:"none",
        rejectIcon:"none",

        accept: () => {
            this.customerService.deleteCustomer(customer)
            .subscribe({
              next: () => {
                this.findAllCustomers();
                this.messageService.add({ severity: 'success', summary: 'Confirmed', detail: 'Record deleted' });
              },
              error: () => {
                this.messageService.add({ severity: 'error', summary: 'Failed', detail: 'Operation failed' });
              }
            });
            
        },
        reject: () => {
            this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected' });
        }
    });
  }
}
