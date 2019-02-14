import { element, by, ElementFinder } from 'protractor';

export class ProductComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-product div table .btn-danger'));
    title = element.all(by.css('jhi-product div h2#page-heading span')).first();

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

export class ProductUpdatePage {
    pageTitle = element(by.id('jhi-product-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    barcodeInput = element(by.id('field_barcode'));
    nameInput = element(by.id('field_name'));
    unitInput = element(by.id('field_unit'));
    warehousePricesInput = element(by.id('field_warehousePrices'));
    unitPricesInput = element(by.id('field_unitPrices'));
    sellingPricesInput = element(by.id('field_sellingPrices'));
    stockInput = element(by.id('field_stock'));
    customerProductSelect = element(by.id('field_customerProduct'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setBarcodeInput(barcode) {
        await this.barcodeInput.sendKeys(barcode);
    }

    async getBarcodeInput() {
        return this.barcodeInput.getAttribute('value');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setUnitInput(unit) {
        await this.unitInput.sendKeys(unit);
    }

    async getUnitInput() {
        return this.unitInput.getAttribute('value');
    }

    async setWarehousePricesInput(warehousePrices) {
        await this.warehousePricesInput.sendKeys(warehousePrices);
    }

    async getWarehousePricesInput() {
        return this.warehousePricesInput.getAttribute('value');
    }

    async setUnitPricesInput(unitPrices) {
        await this.unitPricesInput.sendKeys(unitPrices);
    }

    async getUnitPricesInput() {
        return this.unitPricesInput.getAttribute('value');
    }

    async setSellingPricesInput(sellingPrices) {
        await this.sellingPricesInput.sendKeys(sellingPrices);
    }

    async getSellingPricesInput() {
        return this.sellingPricesInput.getAttribute('value');
    }

    async setStockInput(stock) {
        await this.stockInput.sendKeys(stock);
    }

    async getStockInput() {
        return this.stockInput.getAttribute('value');
    }

    async customerProductSelectLastOption() {
        await this.customerProductSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async customerProductSelectOption(option) {
        await this.customerProductSelect.sendKeys(option);
    }

    getCustomerProductSelect(): ElementFinder {
        return this.customerProductSelect;
    }

    async getCustomerProductSelectedOption() {
        return this.customerProductSelect.element(by.css('option:checked')).getText();
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

export class ProductDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-product-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-product'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
