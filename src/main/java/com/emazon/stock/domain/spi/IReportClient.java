package com.emazon.stock.domain.spi;

import com.emazon.stock.domain.model.Verification;

public interface IReportClient {

    void addVerification(Verification verification);

}
