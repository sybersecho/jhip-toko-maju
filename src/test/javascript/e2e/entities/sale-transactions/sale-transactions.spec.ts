/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SaleTransactionsComponentsPage, SaleTransactionsDeleteDialog, SaleTransactionsUpdatePage } from './sale-transactions.page-object';

const expect = chai.expect;

describe('SaleTransactions e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let saleTransactionsUpdatePage: SaleTransactionsUpdatePage;
    let saleTransactionsComponentsPage: SaleTransactionsComponentsPage;
    let saleTransactionsDeleteDialog: SaleTransactionsDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load SaleTransactions', async () => {
        await navBarPage.goToEntity('sale-transactions');
        saleTransactionsComponentsPage = new SaleTransactionsComponentsPage();
        await browser.wait(ec.visibilityOf(saleTransactionsComponentsPage.title), 5000);
        expect(await saleTransactionsComponentsPage.getTitle()).to.eq('jhiptokomajuApp.saleTransactions.home.title');
    });

    it('should load create SaleTransactions page', async () => {
        await saleTransactionsComponentsPage.clickOnCreateButton();
        saleTransactionsUpdatePage = new SaleTransactionsUpdatePage();
        expect(await saleTransactionsUpdatePage.getPageTitle()).to.eq('jhiptokomajuApp.saleTransactions.home.createOrEditLabel');
        await saleTransactionsUpdatePage.cancel();
    });

    it('should create and save SaleTransactions', async () => {
        const nbButtonsBeforeCreate = await saleTransactionsComponentsPage.countDeleteButtons();

        await saleTransactionsComponentsPage.clickOnCreateButton();
        await promise.all([
            saleTransactionsUpdatePage.setNoInvoiceInput('noInvoice'),
            saleTransactionsUpdatePage.setDiscountInput('5'),
            saleTransactionsUpdatePage.setTotalPaymentInput('5'),
            saleTransactionsUpdatePage.setRemainingPaymentInput('5'),
            saleTransactionsUpdatePage.setPaidInput('5'),
            saleTransactionsUpdatePage.setSaleDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM')
        ]);
        expect(await saleTransactionsUpdatePage.getNoInvoiceInput()).to.eq('noInvoice');
        expect(await saleTransactionsUpdatePage.getDiscountInput()).to.eq('5');
        expect(await saleTransactionsUpdatePage.getTotalPaymentInput()).to.eq('5');
        expect(await saleTransactionsUpdatePage.getRemainingPaymentInput()).to.eq('5');
        expect(await saleTransactionsUpdatePage.getPaidInput()).to.eq('5');
        expect(await saleTransactionsUpdatePage.getSaleDateInput()).to.contain('2001-01-01T02:30');
        await saleTransactionsUpdatePage.save();
        expect(await saleTransactionsUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await saleTransactionsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last SaleTransactions', async () => {
        const nbButtonsBeforeDelete = await saleTransactionsComponentsPage.countDeleteButtons();
        await saleTransactionsComponentsPage.clickOnLastDeleteButton();

        saleTransactionsDeleteDialog = new SaleTransactionsDeleteDialog();
        expect(await saleTransactionsDeleteDialog.getDialogTitle()).to.eq('jhiptokomajuApp.saleTransactions.delete.question');
        await saleTransactionsDeleteDialog.clickOnConfirmButton();

        expect(await saleTransactionsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
