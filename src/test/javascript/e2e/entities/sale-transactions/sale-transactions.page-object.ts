import { element, by, ElementFinder } from 'protractor';

export class SaleTransactionsComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-sale-transactions div table .btn-danger'));
    title = element.all(by.css('jhi-sale-transactions div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SaleTransactionsUpdatePage {
    pageTitle = element(by.id('jhi-sale-transactions-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    noInvoiceInput = element(by.id('field_noInvoice'));
    discountInput = element(by.id('field_discount'));
    totalPaymentInput = element(by.id('field_totalPayment'));
    remainingPaymentInput = element(by.id('field_remainingPayment'));
    paidInput = element(by.id('field_paid'));
    saleDateInput = element(by.id('field_saleDate'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNoInvoiceInput(noInvoice) {
        await this.noInvoiceInput.sendKeys(noInvoice);
    }

    async getNoInvoiceInput() {
        return this.noInvoiceInput.getAttribute('value');
    }

    async setDiscountInput(discount) {
        await this.discountInput.sendKeys(discount);
    }

    async getDiscountInput() {
        return this.discountInput.getAttribute('value');
    }

    async setTotalPaymentInput(totalPayment) {
        await this.totalPaymentInput.sendKeys(totalPayment);
    }

    async getTotalPaymentInput() {
        return this.totalPaymentInput.getAttribute('value');
    }

    async setRemainingPaymentInput(remainingPayment) {
        await this.remainingPaymentInput.sendKeys(remainingPayment);
    }

    async getRemainingPaymentInput() {
        return this.remainingPaymentInput.getAttribute('value');
    }

    async setPaidInput(paid) {
        await this.paidInput.sendKeys(paid);
    }

    async getPaidInput() {
        return this.paidInput.getAttribute('value');
    }

    async setSaleDateInput(saleDate) {
        await this.saleDateInput.sendKeys(saleDate);
    }

    async getSaleDateInput() {
        return this.saleDateInput.getAttribute('value');
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class SaleTransactionsDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-saleTransactions-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-saleTransactions'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
