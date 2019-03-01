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
    unitSelect = element(by.id('field_unit'));
    warehousePriceInput = element(by.id('field_warehousePrice'));
    unitPriceInput = element(by.id('field_unitPrice'));
    sellingPriceInput = element(by.id('field_sellingPrice'));
    stockInput = element(by.id('field_stock'));

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

    async setUnitSelect(unit) {
        await this.unitSelect.sendKeys(unit);
    }

    async getUnitSelect() {
        return this.unitSelect.element(by.css('option:checked')).getText();
    }

    async unitSelectLastOption() {
        await this.unitSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async setWarehousePriceInput(warehousePrice) {
        await this.warehousePriceInput.sendKeys(warehousePrice);
    }

    async getWarehousePriceInput() {
        return this.warehousePriceInput.getAttribute('value');
    }

    async setUnitPriceInput(unitPrice) {
        await this.unitPriceInput.sendKeys(unitPrice);
    }

    async getUnitPriceInput() {
        return this.unitPriceInput.getAttribute('value');
    }

    async setSellingPriceInput(sellingPrice) {
        await this.sellingPriceInput.sendKeys(sellingPrice);
    }

    async getSellingPriceInput() {
        return this.sellingPriceInput.getAttribute('value');
    }

    async setStockInput(stock) {
        await this.stockInput.sendKeys(stock);
    }

    async getStockInput() {
        return this.stockInput.getAttribute('value');
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
