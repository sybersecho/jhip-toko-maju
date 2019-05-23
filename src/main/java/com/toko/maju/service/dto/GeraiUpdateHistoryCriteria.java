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
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the GeraiUpdateHistory entity. This class is used in GeraiUpdateHistoryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /gerai-update-histories?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GeraiUpdateHistoryCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter lastSaleId;

    private InstantFilter createdDate;

    private InstantFilter saleDate;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getLastSaleId() {
        return lastSaleId;
    }

    public void setLastSaleId(LongFilter lastSaleId) {
        this.lastSaleId = lastSaleId;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public InstantFilter getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(InstantFilter saleDate) {
        this.saleDate = saleDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GeraiUpdateHistoryCriteria that = (GeraiUpdateHistoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(lastSaleId, that.lastSaleId) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(saleDate, that.saleDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        lastSaleId,
        createdDate,
        saleDate
        );
    }

    @Override
    public String toString() {
        return "GeraiUpdateHistoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (lastSaleId != null ? "lastSaleId=" + lastSaleId + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (saleDate != null ? "saleDate=" + saleDate + ", " : "") +
            "}";
    }

}
