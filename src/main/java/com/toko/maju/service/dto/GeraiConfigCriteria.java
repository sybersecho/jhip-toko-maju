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
 * Criteria class for the GeraiConfig entity. This class is used in GeraiConfigResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /gerai-configs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GeraiConfigCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codeGerai;

    private StringFilter nameGerai;

    private StringFilter password;

    private StringFilter urlToko;

    private BooleanFilter activated;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCodeGerai() {
        return codeGerai;
    }

    public void setCodeGerai(StringFilter codeGerai) {
        this.codeGerai = codeGerai;
    }

    public StringFilter getNameGerai() {
        return nameGerai;
    }

    public void setNameGerai(StringFilter nameGerai) {
        this.nameGerai = nameGerai;
    }

    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter getUrlToko() {
        return urlToko;
    }

    public void setUrlToko(StringFilter urlToko) {
        this.urlToko = urlToko;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GeraiConfigCriteria that = (GeraiConfigCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(codeGerai, that.codeGerai) &&
            Objects.equals(nameGerai, that.nameGerai) &&
            Objects.equals(password, that.password) &&
            Objects.equals(urlToko, that.urlToko) &&
            Objects.equals(activated, that.activated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        codeGerai,
        nameGerai,
        password,
        urlToko,
        activated
        );
    }

    @Override
    public String toString() {
        return "GeraiConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (codeGerai != null ? "codeGerai=" + codeGerai + ", " : "") +
                (nameGerai != null ? "nameGerai=" + nameGerai + ", " : "") +
                (password != null ? "password=" + password + ", " : "") +
                (urlToko != null ? "urlToko=" + urlToko + ", " : "") +
                (activated != null ? "activated=" + activated + ", " : "") +
            "}";
    }

}
