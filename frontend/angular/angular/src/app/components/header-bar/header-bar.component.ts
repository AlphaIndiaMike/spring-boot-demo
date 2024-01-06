import { Component, ElementRef, ViewChild } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';
import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api/menuitem';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AuthenticationResponse } from '../../models/authentication-response';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header-bar',
  standalone: true,
  imports: [ButtonModule,
    AvatarModule,
    MenuModule,
    ToastModule
    ],
  providers: [MessageService],
  templateUrl: './header-bar.component.html',
  styleUrl: './header-bar.component.scss',
  animations: []
})

export class HeaderBarComponent {

  @ViewChild('menu', { read: ElementRef })
  menu!: ElementRef;
  
  constructor(private messageService: MessageService,
    private router: Router) {}

  items: Array<MenuItem> = [
      {
          label: 'Profile',
          icon: 'pi pi-user'
      },
      {
          label: 'Settings',
          icon: 'pi pi-cog'
      },
      {
        separator: true
      },
      {
          label: 'Sign-out',
          icon: 'pi pi-sign-out',
          command: () => {
            localStorage.clear();
            this.router.navigate(['login']);
          }
      }
  ]

  openMenu() {
    const menuItem = (
      this.menu.nativeElement.getElementsByClassName(
        'p-menuitem-link'
      ) as HTMLCollectionOf<HTMLElement>
    )[0];

    setTimeout(() => {
      menuItem.focus();
    }, 1);
  }

  get username(): string {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const authResp: AuthenticationResponse = JSON.parse(storedUser);
      console.log('authResp:',JSON.stringify(authResp, null, 2));

      console.log('Has own property:', authResp.hasOwnProperty('CustomerDTO'));

      console.log('Type of authResp:', typeof authResp);
      console.log('Type of authResp.CustomerDTO:', typeof authResp.customerDTO);

      
      if (authResp && authResp.customerDTO && authResp.customerDTO.username) {
        return authResp.customerDTO.username ? authResp.customerDTO.username : '--';
      }
      return '---';
    }
    return '----';
  }

  get userrole(): string {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const authResp: AuthenticationResponse = JSON.parse(storedUser);
      if (authResp && authResp.customerDTO) {
        return authResp.customerDTO.roles ? authResp.customerDTO.roles[0] : '--';
      }
      return '---';
    }
    return '----';
  }
}
