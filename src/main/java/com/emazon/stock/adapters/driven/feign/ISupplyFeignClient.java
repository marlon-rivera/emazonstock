package com.emazon.stock.adapters.driven.feign;

import com.emazon.stock.adapters.driving.http.dto.request.SaleRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "emazon-supply", url = "http://localhost:8083/supply")
public interface ISupplyFeignClient {

    @PostMapping("/addSale")
    void addSale(@RequestBody SaleRequest saleRequest);

}
