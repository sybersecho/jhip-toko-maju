/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SupplierComponentsPage, SupplierDeleteDialog, SupplierUpdatePage } from './supplier.page-object';

const expect = chai.expect;

describe('Supplier e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let supplierUpdatePage: SupplierUpdatePage;
    let supplierComponentsPage: SupplierComponentsPage;
    let supplierDeleteDialog: SupplierDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Suppliers', async () => {
        await navBarPage.goToEntity('supplier');
        supplierComponentsPage = new SupplierComponentsPage();
        await browser.wait(ec.visibilityOf(supplierComponentsPage.title), 5000);
        expect(await supplierComponentsPage.getTitle()).to.eq('jhiptokomajuApp.supplier.home.title');
    });

    it('should load create Supplier page', async () => {
        await supplierComponentsPage.clickOnCreateButton();
        supplierUpdatePage = new SupplierUpdatePage();
        expect(await supplierUpdatePage.getPageTitle()).to.eq('jhiptokomajuApp.supplier.home.createOrEditLabel');
        await supplierUpdatePage.cancel();
    });

    it('should create and save Suppliers', async () => {
        const nbButtonsBeforeCreate = await supplierComponentsPage.countDeleteButtons();

        await supplierComponentsPage.clickOnCreateButton();
        await promise.all([
            supplierUpdatePage.setNameInput('name'),
            supplierUpdatePage.setCodeInput('code'),
            supplierUpdatePage.setAddressInput('address'),
            supplierUpdatePage.setNoTelpInput('noTelp'),
            supplierUpdatePage.setBankAccountInput('bankAccount'),
            supplierUpdatePage.setBankNameInput('bankName'),
            supplierUpdatePage.productSelectLastOption()
        ]);
        expect(await supplierUpdatePage.getNameInput()).to.eq('name');
        expect(await supplierUpdatePage.getCodeInput()).to.eq('code');
        expect(await supplierUpdatePage.getAddressInput()).to.eq('address');
        expect(await supplierUpdatePage.getNoTelpInput()).to.eq('noTelp');
        expect(await supplierUpdatePage.getBankAccountInput()).to.eq('bankAccount');
        expect(await supplierUpdatePage.getBankNameInput()).to.eq('bankName');
        await supplierUpdatePage.save();
        expect(await supplierUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await supplierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Supplier', async () => {
        const nbButtonsBeforeDelete = await supplierComponentsPage.countDeleteButtons();
        await supplierComponentsPage.clickOnLastDeleteButton();

        supplierDeleteDialog = new SupplierDeleteDialog();
        expect(await supplierDeleteDialog.getDialogTitle()).to.eq('jhiptokomajuApp.supplier.delete.question');
        await supplierDeleteDialog.clickOnConfirmButton();

        expect(await supplierComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
