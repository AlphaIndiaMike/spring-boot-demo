import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerCardsComponent } from './customer-cards.component';

describe('CustomerCardsComponent', () => {
  let component: CustomerCardsComponent;
  let fixture: ComponentFixture<CustomerCardsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerCardsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomerCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
