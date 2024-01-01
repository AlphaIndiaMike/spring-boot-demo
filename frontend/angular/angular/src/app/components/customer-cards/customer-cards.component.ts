import { Component, Input, OnInit } from '@angular/core';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { BadgeModule } from 'primeng/badge';
import { CustomerDTO } from '../../models/customer-dto';

@Component({
  selector: 'app-customer-cards',
  standalone: true,
  imports: [CardModule, ButtonModule, BadgeModule],
  templateUrl: './customer-cards.component.html',
  styleUrl: './customer-cards.component.scss'
})
export class CustomerCardsComponent implements OnInit {
  @Input()
  customer: CustomerDTO = {};

  randomUserGender: string ="";
  customerId: string="";
  imageUrl: string="";

  ngOnInit(): void {
    this.randomUserGender = this.customer.gender === "MALE" ? "men" : "women";
    this.customerId=this.customer.id ? (this.customer.id%50).toString() : '';
    this.imageUrl = `https://randomuser.me/api/portraits/${this.randomUserGender}/${this.customerId}.jpg`;

  }

  }
