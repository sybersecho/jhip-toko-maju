/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ReturnTransactionService } from 'app/entities/return-transaction/return-transaction.service';
import { IReturnTransaction, ReturnTransaction, TransactionType } from 'app/shared/model/return-transaction.model';

describe('Service Tests', () => {
    describe('ReturnTransaction Service', () => {
        let injector: TestBed;
        let service: ReturnTransactionService;
        let httpMock: HttpTestingController;
        let elemDefault: IReturnTransaction;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ReturnTransactionService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new ReturnTransaction(0, currentDate, TransactionType.SHOP, 0);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        created_date: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a ReturnTransaction', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        created_date: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        created_date: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new ReturnTransaction(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a ReturnTransaction', async () => {
                const returnedFromService = Object.assign(
                    {
                        created_date: currentDate.format(DATE_TIME_FORMAT),
                        transactionType: 'BBBBBB',
                        totalPriceReturn: 1
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        created_date: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of ReturnTransaction', async () => {
                const returnedFromService = Object.assign(
                    {
                        created_date: currentDate.format(DATE_TIME_FORMAT),
                        transactionType: 'BBBBBB',
                        totalPriceReturn: 1
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        created_date: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a ReturnTransaction', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
