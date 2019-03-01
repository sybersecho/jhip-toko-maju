import { element, by, ElementFinder } from 'protractor';

export class SupplierComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-supplier div table .btn-danger'));
    title = element.all(by.css('jhi-supplier div h2#page-heading span')).first();

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

export class SupplierUpdatePage {
    pageTitle = element(by.id('jhi-supplier-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    codeInput = element(by.id('field_code'));
    addressInput = element(by.id('field_address'));
    noTelpInput = element(by.id('field_noTelp'));
    bankAccountInput = element(by.id('field_bankAccount'));
    bankNameInput = element(by.id('field_bankName'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setCodeInput(code) {
        await this.codeInput.sendKeys(code);
    }

    async getCodeInput() {
        return this.codeInput.getAttribute('value');
    }

    async setAddressInput(address) {
        await this.addressInput.sendKeys(address);
    }

    async getAddressInput() {
        return this.addressInput.getAttribute('value');
    }

    async setNoTelpInput(noTelp) {
        await this.noTelpInput.sendKeys(noTelp);
    }

    async getNoTelpInput() {
        return this.noTelpInput.getAttribute('value');
    }

    async setBankAccountInput(bankAccount) {
        await this.bankAccountInput.sendKeys(bankAccount);
    }

    async getBankAccountInput() {
        return this.bankAccountInput.getAttribute('value');
    }

    async setBankNameInput(bankName) {
        await this.bankNameInput.sendKeys(bankName);
    }

    async getBankNameInput() {
        return this.bankNameInput.getAttribute('value');
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

export class SupplierDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-supplier-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-supplier'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
