import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CustomerDTO } from '../../models/customer-dto';
import { environment } from '../../../environments/environment.development';

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
}
