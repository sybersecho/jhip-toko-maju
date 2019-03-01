package com.toko.maju.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Supplier entity. This class is used in SupplierResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /suppliers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SupplierCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private StringFilter address;

    private StringFilter noTelp;

    private StringFilter bankAccount;

    private StringFilter bankName;

    private LongFilter productId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(StringFilter noTelp) {
        this.noTelp = noTelp;
    }

    public StringFilter getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(StringFilter bankAccount) {
        this.bankAccount = bankAccount;
    }

    public StringFilter getBankName() {
        return bankName;
    }

    public void setBankName(StringFilter bankName) {
        this.bankName = bankName;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SupplierCriteria that = (SupplierCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(address, that.address) &&
            Objects.equals(noTelp, that.noTelp) &&
            Objects.equals(bankAccount, that.bankAccount) &&
            Objects.equals(bankName, that.bankName) &&
            Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        code,
        address,
        noTelp,
        bankAccount,
        bankName,
        productId
        );
    }

    @Override
    public String toString() {
        return "SupplierCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (noTelp != null ? "noTelp=" + noTelp + ", " : "") +
                (bankAccount != null ? "bankAccount=" + bankAccount + ", " : "") +
                (bankName != null ? "bankName=" + bankName + ", " : "") +
                (productId != null ? "productId=" + productId + ", " : "") +
            "}";
    }

}
