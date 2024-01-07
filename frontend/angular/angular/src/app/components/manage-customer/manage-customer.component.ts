import { Component, EventEmitter, Input, Output } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CustomerRegistrationRequest } from '../../models/customer-registration-request';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-manage-customer',
  standalone: true,
  imports: [InputTextModule,
            ButtonModule,
            FormsModule,
            CommonModule],
  templateUrl: './manage-customer.component.html',
  styleUrl: './manage-customer.component.scss'
})
export class ManageCustomerComponent{

  value: string = "";

  @Input()
  customer: CustomerRegistrationRequest = {};

  @Input()
  operation: 'create' | 'update' = 'create';

  @Output()
  submit: EventEmitter<CustomerRegistrationRequest> = new EventEmitter<CustomerRegistrationRequest>;

  @Output()
  cancel: EventEmitter<void> = new EventEmitter<void>;

  title = 'New customer';

  get isCustomerValid(): boolean {
    return this.hasLen(this.customer.name) && 
    this.hasLen(this.customer.email) && 
    this.customer.age !== undefined && this.customer.age > 17 && (
      this.operation === 'update' || 
      this.hasLen(this.customer.password) && 
      this.hasLen(this.customer.gender)
    );
  }

  private hasLen(input: string | undefined): boolean{
    return input !== null && input !== undefined && input.length>3
  }

  onSubmit() {
    this.submit.emit(this.customer);
  }

  onCancel() {
    this.cancel.emit();
  }
}
