package com.toko.maju.web.rest.vm;

import com.toko.maju.service.dto.ReturnTransactionDTO;

public class ReturnTransactionVM extends ReturnTransactionDTO {
    private Boolean cashReturn;

    public Boolean getCashReturn() {
        return cashReturn;
    }

    public void setCashReturn(Boolean cashReturn) {
        this.cashReturn = cashReturn;
    }
}
