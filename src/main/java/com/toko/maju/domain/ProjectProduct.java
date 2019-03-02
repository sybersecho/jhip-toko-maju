package com.toko.maju.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A ProjectProduct.
 */
@Entity
@Table(name = "project_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projectproduct")
public class ProjectProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "special_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal specialPrice;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("projectProducts")
    private Product product;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("products")
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSpecialPrice() {
        return specialPrice;
    }

    public ProjectProduct specialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
        return this;
    }

    public void setSpecialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
    }

    public Product getProduct() {
        return product;
    }

    public ProjectProduct product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Project getProject() {
        return project;
    }

    public ProjectProduct project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
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
        ProjectProduct projectProduct = (ProjectProduct) o;
        if (projectProduct.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectProduct.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectProduct{" +
            "id=" + getId() +
            ", specialPrice=" + getSpecialPrice() +
            "}";
    }
}
