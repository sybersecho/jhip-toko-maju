/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CustomerProductComponentsPage, CustomerProductDeleteDialog, CustomerProductUpdatePage } from './customer-product.page-object';

const expect = chai.expect;

describe('CustomerProduct e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let customerProductUpdatePage: CustomerProductUpdatePage;
    let customerProductComponentsPage: CustomerProductComponentsPage;
    let customerProductDeleteDialog: CustomerProductDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load CustomerProducts', async () => {
        await navBarPage.goToEntity('customer-product');
        customerProductComponentsPage = new CustomerProductComponentsPage();
        await browser.wait(ec.visibilityOf(customerProductComponentsPage.title), 5000);
        expect(await customerProductComponentsPage.getTitle()).to.eq('jhiptokomajuApp.customerProduct.home.title');
    });

    it('should load create CustomerProduct page', async () => {
        await customerProductComponentsPage.clickOnCreateButton();
        customerProductUpdatePage = new CustomerProductUpdatePage();
        expect(await customerProductUpdatePage.getPageTitle()).to.eq('jhiptokomajuApp.customerProduct.home.createOrEditLabel');
        await customerProductUpdatePage.cancel();
    });

    it('should create and save CustomerProducts', async () => {
        const nbButtonsBeforeCreate = await customerProductComponentsPage.countDeleteButtons();

        await customerProductComponentsPage.clickOnCreateButton();
        await promise.all([customerProductUpdatePage.setSpecialPriceInput('5'), customerProductUpdatePage.customerSelectLastOption()]);
        expect(await customerProductUpdatePage.getSpecialPriceInput()).to.eq('5');
        await customerProductUpdatePage.save();
        expect(await customerProductUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await customerProductComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last CustomerProduct', async () => {
        const nbButtonsBeforeDelete = await customerProductComponentsPage.countDeleteButtons();
        await customerProductComponentsPage.clickOnLastDeleteButton();

        customerProductDeleteDialog = new CustomerProductDeleteDialog();
        expect(await customerProductDeleteDialog.getDialogTitle()).to.eq('jhiptokomajuApp.customerProduct.delete.question');
        await customerProductDeleteDialog.clickOnConfirmButton();

        expect(await customerProductComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
