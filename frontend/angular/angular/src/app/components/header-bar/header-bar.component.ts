import { Component, ElementRef, ViewChild } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';
import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api/menuitem';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

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
  
  constructor(private messageService: MessageService) {}

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
          icon: 'pi pi-sign-out'
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
}
