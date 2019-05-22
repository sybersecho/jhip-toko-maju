package com.toko.maju.web.rest.vm;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReportPaymentVM {
    private String noInvoice;
    private String customer;
    private String project;
    private Instant saleDate;
    private BigDecimal totalPayment;

    private List<ReportPaymentDetailVM> paymentDetails = new ArrayList<>();

    public String getNoInvoice() {
        return noInvoice;
    }

    public void setNoInvoice(String noInvoice) {
        this.noInvoice = noInvoice;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Instant getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Instant saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public List<ReportPaymentDetailVM> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<ReportPaymentDetailVM> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    @Override
    public String toString() {
        return "ReportPaymentVM{" +
            "noInvoice='" + noInvoice + '\'' +
            ", customer='" + customer + '\'' +
            ", project='" + project + '\'' +
            ", saleDate=" + saleDate +
            ", totalPayment=" + totalPayment +
            ", paymentDetails=" + paymentDetails +
            '}';
    }
}
