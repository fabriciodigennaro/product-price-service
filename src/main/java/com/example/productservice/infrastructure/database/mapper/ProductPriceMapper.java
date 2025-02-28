package com.example.productservice.infrastructure.database.mapper;

import com.example.productservice.domain.productprice.BrandId;
import com.example.productservice.domain.productprice.Price;
import com.example.productservice.domain.productprice.ProductId;
import com.example.productservice.domain.productprice.ProductPrice;
import com.example.productservice.infrastructure.database.entity.ProductPriceEntity;

import javax.money.Monetary;

public class ProductPriceMapper {

    private ProductPriceMapper() {}

    public static ProductPrice toDomain(ProductPriceEntity entity) {
        return new ProductPrice(
            new BrandId(entity.getBrandId()),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getPriceList(),
            new ProductId(entity.getProductId()),
            entity.getPriority(),
            new Price(
                entity.getPrice(),
                Monetary.getCurrency(entity.getCurrency())
            )
        );
    }
}
