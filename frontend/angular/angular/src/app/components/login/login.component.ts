import { Component } from '@angular/core';
import { AvatarModule } from 'primeng/avatar';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { AuthenticationRequest } from '../../models/authentication-request';
import { FormsModule } from '@angular/forms';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { MessagesModule } from 'primeng/messages';
import { Message } from 'primeng/api';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [AvatarModule,
            InputTextModule,
            ButtonModule,
            FormsModule,
            MessagesModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  authReq: AuthenticationRequest = {};
  errorMsg: string = '';

  constructor(private authServ: AuthenticationService,
    private router: Router){}

  messages: Message[] = [];

  addMessages() {
      this.messages = [
          { severity: 'error', summary: 'Error', detail: this.errorMsg }
      ];
  }

  clearMessages() {
      this.messages = [];
  }

  login() {
    this.clearMessages();
    this.errorMsg = '';
    this.authServ.login(this.authReq)
    .subscribe({
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
  }
}
