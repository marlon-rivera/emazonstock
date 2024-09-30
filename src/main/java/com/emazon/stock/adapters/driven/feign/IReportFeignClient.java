package com.emazon.stock.adapters.driven.feign;

import com.emazon.stock.domain.model.Verification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "emazon-report", url = "http://localhost:8085/report")
public interface IReportFeignClient {

    @PostMapping("/addVerification")
    void addVerification(@RequestBody Verification verification);

}
