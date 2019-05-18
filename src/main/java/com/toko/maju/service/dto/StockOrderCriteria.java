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
 * Criteria class for the StockOrder entity. This class is used in StockOrderResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /stock-orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockOrderCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter siteLocation;

    private InstantFilter createdDate;

    private BooleanFilter processed;

    private InstantFilter processedDate;

    private LongFilter creatorId;

    private LongFilter approvalId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(StringFilter siteLocation) {
        this.siteLocation = siteLocation;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public BooleanFilter getProcessed() {
        return processed;
    }

    public void setProcessed(BooleanFilter processed) {
        this.processed = processed;
    }

    public InstantFilter getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(InstantFilter processedDate) {
        this.processedDate = processedDate;
    }

    public LongFilter getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(LongFilter creatorId) {
        this.creatorId = creatorId;
    }

    public LongFilter getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(LongFilter approvalId) {
        this.approvalId = approvalId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StockOrderCriteria that = (StockOrderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(siteLocation, that.siteLocation) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(processed, that.processed) &&
            Objects.equals(processedDate, that.processedDate) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(approvalId, that.approvalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        siteLocation,
        createdDate,
        processed,
        processedDate,
        creatorId,
        approvalId
        );
    }

    @Override
    public String toString() {
        return "StockOrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (siteLocation != null ? "siteLocation=" + siteLocation + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (processed != null ? "processed=" + processed + ", " : "") +
                (processedDate != null ? "processedDate=" + processedDate + ", " : "") +
                (creatorId != null ? "creatorId=" + creatorId + ", " : "") +
                (approvalId != null ? "approvalId=" + approvalId + ", " : "") +
            "}";
    }

}
