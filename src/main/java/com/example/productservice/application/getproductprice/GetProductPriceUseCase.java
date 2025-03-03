package com.example.productservice.application.getproductprice;

import com.example.productservice.application.UseCase;
import com.example.productservice.application.getproductprice.GetProductPriceResponse.Successful;
import com.example.productservice.domain.productprice.ProductPrice;
import com.example.productservice.domain.productprice.ProductPriceRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.example.productservice.application.getproductprice.GetProductPriceResponse.*;

public class GetProductPriceUseCase implements UseCase<GetProductPriceRequest, GetProductPriceResponse> {

    private final ProductPriceRepository productPriceRepository;

    public GetProductPriceUseCase(ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    @Override
    public GetProductPriceResponse execute(GetProductPriceRequest request) {
        List<ProductPrice> productPrices = productPriceRepository.getProductPrices(
            request.productId(),
            request.brandId(),
            request.validAt()
        );

        Optional<ProductPrice> productPrice = productPrices.stream()
            .max(Comparator.comparingInt(ProductPrice::priority));

        return productPrice.isPresent() ? new Successful(productPrice.get()) : new ProductPriceNotFound();
    }
}
