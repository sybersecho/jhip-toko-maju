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
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the SaleTransactions entity. This class is used in SaleTransactionsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sale-transactions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SaleTransactionsCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter noInvoice;

    private BigDecimalFilter discount;

    private BigDecimalFilter totalPayment;

    private BigDecimalFilter remainingPayment;

    private BigDecimalFilter paid;

    private InstantFilter saleDate;

    private BooleanFilter settled;

    private LongFilter itemsId;

    private LongFilter customerId;

    private LongFilter creatorId;

    private LongFilter duePaymentId;

    private LongFilter projectId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNoInvoice() {
        return noInvoice;
    }

    public void setNoInvoice(StringFilter noInvoice) {
        this.noInvoice = noInvoice;
    }

    public BigDecimalFilter getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimalFilter discount) {
        this.discount = discount;
    }

    public BigDecimalFilter getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimalFilter totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimalFilter getRemainingPayment() {
        return remainingPayment;
    }

    public void setRemainingPayment(BigDecimalFilter remainingPayment) {
        this.remainingPayment = remainingPayment;
    }

    public BigDecimalFilter getPaid() {
        return paid;
    }

    public void setPaid(BigDecimalFilter paid) {
        this.paid = paid;
    }

    public InstantFilter getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(InstantFilter saleDate) {
        this.saleDate = saleDate;
    }

    public BooleanFilter getSettled() {
        return settled;
    }

    public void setSettled(BooleanFilter settled) {
        this.settled = settled;
    }

    public LongFilter getItemsId() {
        return itemsId;
    }

    public void setItemsId(LongFilter itemsId) {
        this.itemsId = itemsId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }

    public LongFilter getDuePaymentId() {
        return duePaymentId;
    }

    public void setDuePaymentId(LongFilter duePaymentId) {
        this.duePaymentId = duePaymentId;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SaleTransactionsCriteria that = (SaleTransactionsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(noInvoice, that.noInvoice) &&
            Objects.equals(discount, that.discount) &&
            Objects.equals(totalPayment, that.totalPayment) &&
            Objects.equals(remainingPayment, that.remainingPayment) &&
            Objects.equals(paid, that.paid) &&
            Objects.equals(saleDate, that.saleDate) &&
            Objects.equals(settled, that.settled) &&
            Objects.equals(itemsId, that.itemsId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(duePaymentId, that.duePaymentId) &&
            Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        noInvoice,
        discount,
        totalPayment,
        remainingPayment,
        paid,
        saleDate,
        settled,
        itemsId,
        customerId,
        creatorId,
        duePaymentId,
        projectId
        );
    }

    @Override
    public String toString() {
        return "SaleTransactionsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (noInvoice != null ? "noInvoice=" + noInvoice + ", " : "") +
                (discount != null ? "discount=" + discount + ", " : "") +
                (totalPayment != null ? "totalPayment=" + totalPayment + ", " : "") +
                (remainingPayment != null ? "remainingPayment=" + remainingPayment + ", " : "") +
                (paid != null ? "paid=" + paid + ", " : "") +
                (saleDate != null ? "saleDate=" + saleDate + ", " : "") +
                (settled != null ? "settled=" + settled + ", " : "") +
                (itemsId != null ? "itemsId=" + itemsId + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (duePaymentId != null ? "duePaymentId=" + duePaymentId + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
            "}";
    }

}
