package com.example.bookstore.service.projection;

import java.math.BigDecimal;
import java.time.Instant;

public interface SalePaymentProjection {

    Long getSaleId();
    Instant getSoldAt();
    BigDecimal getAmount();
    String getCurrency();
    String getTitle();
    String getAuthor();
    Long getBookInstanceId();
}
