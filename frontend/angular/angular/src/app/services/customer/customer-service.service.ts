import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CustomerDTO } from '../../models/customer-dto';
import { environment } from '../../../environments/environment.development';
import { CustomerRegistrationRequest } from '../../models/customer-registration-request';
import { CustomerUpdateRequest } from '../../models/customer-update-request';

@Injectable({
  providedIn: 'root'
})
export class CustomerServiceService {

  private readonly customerUrl = `${environment.api.baseUrl}${environment.api.customersUrl}`;

  constructor(
    private http: HttpClient
  ) { }

  findAll() : Observable<CustomerDTO[]> {
    return this.http.get<CustomerDTO[]>(this.customerUrl);
  }

  registerCustomer(customer: CustomerRegistrationRequest): Observable<void> {
    return this.http.post<void>(this.customerUrl, customer);
  }

  deleteCustomer(customer: CustomerDTO | undefined) : Observable<void> {
    return this.http.delete<void>(`${this.customerUrl}/${customer?.id?.toString()}`);
  }

  updateCustomer(cid: number | undefined, customer: CustomerUpdateRequest) : Observable<void> {
    return this.http.put<void>(`${this.customerUrl}/${cid?.toString()}`, customer);
  }
}
