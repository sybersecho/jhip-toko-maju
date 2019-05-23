package com.toko.maju.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A GeraiConfig.
 */
@Entity
@Table(name = "gerai_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "geraiconfig")
public class GeraiConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 10)
    @Column(name = "code_gerai", length = 10, nullable = false, unique = true)
    private String codeGerai;

    @NotNull
    @Column(name = "name_gerai", nullable = false)
    private String nameGerai;

    @NotNull
    @Size(min = 5)
    @Column(name = "jhi_password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "url_toko", nullable = false)
    private String urlToko;

    @NotNull
    @Column(name = "activated", nullable = false)
    private Boolean activated;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeGerai() {
        return codeGerai;
    }

    public GeraiConfig codeGerai(String codeGerai) {
        this.codeGerai = codeGerai;
        return this;
    }

    public void setCodeGerai(String codeGerai) {
        this.codeGerai = codeGerai;
    }

    public String getNameGerai() {
        return nameGerai;
    }

    public GeraiConfig nameGerai(String nameGerai) {
        this.nameGerai = nameGerai;
        return this;
    }

    public void setNameGerai(String nameGerai) {
        this.nameGerai = nameGerai;
    }

    public String getPassword() {
        return password;
    }

    public GeraiConfig password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlToko() {
        return urlToko;
    }

    public GeraiConfig urlToko(String urlToko) {
        this.urlToko = urlToko;
        return this;
    }

    public void setUrlToko(String urlToko) {
        this.urlToko = urlToko;
    }

    public Boolean isActivated() {
        return activated;
    }

    public GeraiConfig activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeraiConfig geraiConfig = (GeraiConfig) o;
        if (geraiConfig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), geraiConfig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GeraiConfig{" +
            "id=" + getId() +
            ", codeGerai='" + getCodeGerai() + "'" +
            ", nameGerai='" + getNameGerai() + "'" +
            ", password='" + getPassword() + "'" +
            ", urlToko='" + getUrlToko() + "'" +
            ", activated='" + isActivated() + "'" +
            "}";
    }
}
