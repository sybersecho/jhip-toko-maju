package com.toko.maju.web.rest.vm;

import java.math.BigDecimal;
import java.time.Instant;

public class ReportPaymentDetailVM {
    private Instant paymentDate;
    private BigDecimal remainingPayment;
    private BigDecimal paid;

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getRemainingPayment() {
        return remainingPayment;
    }

    public void setRemainingPayment(BigDecimal remainingPayment) {
        this.remainingPayment = remainingPayment;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "ReportPaymentDetailVM{" +
            "paymentDate=" + paymentDate +
            ", remainingPayment=" + remainingPayment +
            ", paid=" + paid +
            '}';
    }
}
