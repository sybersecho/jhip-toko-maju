import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ISaleTransactions, SaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleTransactionsService } from '../sale-transactions.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { SaleItem, ISaleItem } from 'app/shared/model/sale-item.model';

@Component({
    selector: 'jhi-delivery-orders',
    templateUrl: './delivery-orders.component.html',
    styleUrls: ['delivery-orders.css']
})
export class DeliveryOrdersComponent implements OnInit {
    saleTransaction: ISaleTransactions = new SaleTransactions();
    saleDetails: Promise<ISaleTransactions>;
    invoiceNo: string;
    items: ISaleItem[];
    constructor(private router: Router, protected activatedRoute: ActivatedRoute, protected saleService: SaleTransactionsService) {
        this.invoiceNo = this.activatedRoute.snapshot.params['invoiceNo'];
        // console.log(this.activatedRoute);
    }

    ngOnInit() {
        console.log(this.invoiceNo);
        if (this.invoiceNo) {
            this.saleDetails = this.saleService
                .findByInvoice(this.invoiceNo)
                .toPromise()
                .then(response => {
                    const sale = response.body;
                    this.saleTransaction = sale[0];
                    this.items = this.saleTransaction.items;
                    console.log(sale);
                    return sale[0];
                })
                .catch(err => {
                    console.error(err.message);
                    return null;
                });
            // this.subscripeResponse(this.saleService.findByInvoice(this.invoiceNo));
        }
        Promise.resolve(this.saleDetails).then(() =>
            setTimeout(() => {
                console.log('open print');
                window.print();
                this.router.navigate([{ outlets: { print: null } }]);
            })
        );
    }

    subscripeResponse(result: Observable<HttpResponse<ISaleTransactions[]>>): void {
        result.subscribe(
            res => {
                console.log('Res_0: ');
                console.log(res.body[0]);
                if (res) {
                    this.saleTransaction = res.body[0];
                    // this.saleDetails = this.setToPromise(this.saleTransaction);
                    console.log('sale: ');
                    console.log(this.saleTransaction);
                    console.log('item size: ' + this.saleTransaction.items.length);
                    console.log(this.saleTransaction.items);
                    // setTimeout(() => {
                    window.print();
                    // }, 1000);
                }

                this.router.navigate([{ outlets: { print: null } }]);
            },
            err => {
                console.log(err.message);
                this.router.navigate([{ outlets: { print: null } }]);
            }
        );
    }

    // protected subscribeCheck()
}
