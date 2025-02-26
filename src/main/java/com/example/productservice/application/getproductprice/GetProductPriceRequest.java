package com.example.productservice.application.getproductprice;

import com.example.productservice.domain.productprice.BrandId;
import com.example.productservice.domain.productprice.ProductId;

import java.time.LocalDateTime;

public record GetProductPriceRequest(
    ProductId productId,
    BrandId brandId,
    LocalDateTime validAt
) {}
