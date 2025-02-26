package com.example.productservice.application.getproductprice;

import com.example.productservice.domain.productprice.ProductPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.example.productservice.application.getproductprice.GetProductPriceResponse.*;

public abstract sealed class GetProductPriceResponse permits Successful, ProductPriceNotFound {

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static final class Successful extends GetProductPriceResponse {
        private final ProductPrice productPrice;
    }
    public static final class ProductPriceNotFound extends GetProductPriceResponse {}
}
