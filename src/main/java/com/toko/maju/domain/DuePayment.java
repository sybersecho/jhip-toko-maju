package com.toko.maju.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A DuePayment.
 */
@Entity
@Table(name = "due_payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "duepayment")
public class DuePayment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "remaining_payment", precision = 10, scale = 2)
	private BigDecimal remainingPayment;

	@NotNull
	@Column(name = "created_date", nullable = false)
	private Instant createdDate;

	@NotNull
	@Column(name = "settled", nullable = false)
	private Boolean settled;

	@NotNull
	@DecimalMin(value = "0")
	@Column(name = "paid", precision = 10, scale = 2, nullable = false)
	private BigDecimal paid;

	@NotNull
	@DecimalMin(value = "0")
	@Column(name = "total_payment", precision = 10, scale = 2, nullable = false)
	private BigDecimal totalPayment;

	@ManyToOne(optional = false)
	@NotNull
	@JsonIgnoreProperties("duePayments")
	private User creator;

	@ManyToOne(optional = false)
//    @JsonIgnoreProperties("duePayments")
	@JsonBackReference
	private SaleTransactions sale;

	// jhipster-needle-entity-add-field - JHipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getRemainingPayment() {
		return remainingPayment;
	}

	public DuePayment remainingPayment(BigDecimal remainingPayment) {
		this.remainingPayment = remainingPayment;
		return this;
	}

	public void setRemainingPayment(BigDecimal remainingPayment) {
		this.remainingPayment = remainingPayment;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public DuePayment createdDate(Instant createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean isSettled() {
		return settled;
	}

	public DuePayment settled(Boolean settled) {
		this.settled = settled;
		return this;
	}

	public void setSettled(Boolean settled) {
		this.settled = settled;
	}

	public BigDecimal getPaid() {
		return paid;
	}

	public DuePayment paid(BigDecimal paid) {
		this.paid = paid;
		return this;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public BigDecimal getTotalPayment() {
		return totalPayment;
	}

	public DuePayment totalPayment(BigDecimal totalPayment) {
		this.totalPayment = totalPayment;
		return this;
	}

	public void setTotalPayment(BigDecimal totalPayment) {
		this.totalPayment = totalPayment;
	}

	public User getCreator() {
		return creator;
	}

	public DuePayment creator(User user) {
		this.creator = user;
		return this;
	}

	public void setCreator(User user) {
		this.creator = user;
	}

	public SaleTransactions getSale() {
		return sale;
	}

	public DuePayment sale(SaleTransactions saleTransactions) {
		this.sale = saleTransactions;
		return this;
	}

	public void setSale(SaleTransactions saleTransactions) {
		this.sale = saleTransactions;
	}
	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here, do not remove

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DuePayment duePayment = (DuePayment) o;
		if (duePayment.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), duePayment.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "DuePayment{" + "id=" + getId() + ", remainingPayment=" + getRemainingPayment() + ", createdDate='"
				+ getCreatedDate() + "'" + ", settled='" + isSettled() + "'" + ", paid=" + getPaid() + ", totalPayment="
				+ getTotalPayment() + "}";
	}
}
