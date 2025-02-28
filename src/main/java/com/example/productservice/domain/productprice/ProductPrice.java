package com.example.productservice.domain.productprice;

import java.time.LocalDateTime;

public record ProductPrice(
    BrandId brandId,
    LocalDateTime startDate,
    LocalDateTime endDate,
    int priceList,
    ProductId productId,
    int priority,
    Price price
) {}
