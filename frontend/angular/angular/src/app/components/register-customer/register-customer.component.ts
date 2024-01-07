import { Component } from '@angular/core';
import { AvatarModule } from 'primeng/avatar';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { MessagesModule } from 'primeng/messages';
import { Message } from 'primeng/api';
import { Router } from '@angular/router';
import { CustomerRegistrationRequest } from '../../models/customer-registration-request';
import { CustomerServiceService } from '../../services/customer/customer-service.service';
import { AuthenticationRequest } from '../../models/authentication-request';
import { AuthenticationResponse } from '../../models/authentication-response';

@Component({
  selector: 'app-register-customer',
  standalone: true,
  imports: [AvatarModule,
    InputTextModule,
    ButtonModule,
    FormsModule,
    MessagesModule],
  templateUrl: './register-customer.component.html',
  styleUrl: './register-customer.component.scss'
})
export class RegisterCustomerComponent {
  regReq: CustomerRegistrationRequest = {};
  errorMsg: string = '';

  constructor(private authServ: AuthenticationService,
    private router: Router,
    private customerService: CustomerServiceService,
    private authSvc : AuthenticationService){}

  messages: Message[] = [];

  addMessages() {
      this.messages = [
          { severity: 'error', summary: 'Error', detail: this.errorMsg }
      ];
  }

  private hasLen(input: string | undefined): boolean{
    return input !== null && input !== undefined && input.length>3
  }

  get isCustomerValid(): boolean {
    return this.hasLen(this.regReq.name) && 
    this.hasLen(this.regReq.email) && 
    this.regReq.age !== undefined && this.regReq.age > 17 && 
    this.hasLen(this.regReq.password) && 
    this.hasLen(this.regReq.gender)
    ;
  }

  clearMessages() {
      this.messages = [];
  }

  register(){
    if (this.isCustomerValid){
      console.log(this.regReq);
      this.customerService.registerCustomer(this.regReq)
      .subscribe({
        next: () => {
          const authReq: AuthenticationRequest = {
            username: this.regReq.email,
            password: this.regReq.password
          }
          this.authSvc.login(authReq).subscribe({
            next: (authenticationResponse) => {
              localStorage.setItem('user', JSON.stringify(authenticationResponse));
              this.router.navigate(['customers']);
            },
            error: (err) => {
              if (err.error.statusCode === 401) {
                this.errorMsg = 'Login failed.';
                this.addMessages();
              }
            }
          })
        },
        error: (err) => {
          this.errorMsg = 'Registration failed.';
          this.addMessages();
        }
      })
    } else{
        this.errorMsg = 'Customer data invalid!';
        this.addMessages();
    }

  }

  goToLogin(){
    this.router.navigate(['login']);
  }
}
