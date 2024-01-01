import { TestBed } from '@angular/core/testing';

import { HttpUserTokenService } from './http-user-token.service';

describe('HttpUserTokenService', () => {
  let service: HttpUserTokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HttpUserTokenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
