import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiptokomajuCustomerModule } from './customer/customer.module';
import { JhiptokomajuProductModule } from './product/product.module';
import { JhiptokomajuProjectModule } from './project/project.module';
import { JhiptokomajuSupplierModule } from './supplier/supplier.module';

@NgModule({
    imports: [
        JhiptokomajuCustomerModule,
        JhiptokomajuProductModule,
        JhiptokomajuProjectModule,
        JhiptokomajuSupplierModule
        // RouterModule.forChild([
        //     {
        //         path: 'customer',
        //         loadChildren: './customer/customer.module#JhiptokomajuCustomerModule'
        //     },
        //     {
        //         path: 'product',
        //         loadChildren: './product/product.module#JhiptokomajuProductModule'
        //     },
        //     {
        //         path: 'project',
        //         loadChildren: './project/project.module#JhiptokomajuProjectModule'
        //     },
        //     {
        //         path: 'supplier',
        //         loadChildren: './supplier/supplier.module#JhiptokomajuSupplierModule'
        //     }
        // //     /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        // ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuEntityModule {}
