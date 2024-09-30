package com.emazon.stock.adapters.driven.feign;

import com.emazon.stock.domain.model.Verification;
import com.emazon.stock.domain.spi.IReportClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReportFeignAdapter implements IReportClient {

    private final IReportFeignClient reportFeignClient;

    @Override
    public void addVerification(Verification verification) {
        reportFeignClient.addVerification(verification);
    }
}
