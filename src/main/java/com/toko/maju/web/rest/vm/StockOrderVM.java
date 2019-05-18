package com.toko.maju.web.rest.vm;

import com.toko.maju.domain.StockOrderRequest;
import com.toko.maju.service.dto.StockOrderDTO;
import com.toko.maju.service.dto.StockOrderRequestDTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class StockOrderVM extends StockOrderDTO {
    private List<StockOrderRequestDTO> stockOrderRequests = new ArrayList<>();

    public List<StockOrderRequestDTO> getStockOrderRequests() {
        return stockOrderRequests;
    }

    public void setStockOrderRequests(List<StockOrderRequestDTO> stockOrderRequests) {
        this.stockOrderRequests = stockOrderRequests;
    }

    @Override
    public String toString() {
        return "StockOrderVM{" +
            "stockOrderRequests=" + stockOrderRequests +
            '}';
    }

    public void setCurrentDate() {
        this.stockOrderRequests.forEach(it -> it.setCreatedDate(Instant.now()));
    }

    public void setOrder(StockOrderDTO result) {
        this.stockOrderRequests.forEach(it -> it.setStockOrderId(result.getId()));
    }
}
