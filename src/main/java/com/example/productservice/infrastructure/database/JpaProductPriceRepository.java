package com.example.productservice.infrastructure.database;

import com.example.productservice.domain.productprice.BrandId;
import com.example.productservice.domain.productprice.ProductId;
import com.example.productservice.domain.productprice.ProductPrice;
import com.example.productservice.domain.productprice.ProductPriceRepository;
import com.example.productservice.infrastructure.database.entity.ProductPriceEntity;
import com.example.productservice.infrastructure.database.mapper.ProductPriceMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JpaProductPriceRepository extends JpaRepository<ProductPriceEntity, UUID>, ProductPriceRepository {
    @Query("""
        SELECT p FROM ProductPriceEntity p
        WHERE p.productId = :productId
        AND p.brandId = :brandId
        AND p.startDate <= :validAt
        AND p.endDate >= :validAt
    """)
    List<ProductPriceEntity> findProductPrices(
        @Param("productId") Long productId,
        @Param("brandId") Long brandId,
        @Param("validAt") LocalDateTime validAt
    );

    @Override
    default List<ProductPrice> getProductPrices(ProductId productId, BrandId brandId, LocalDateTime validAt) {
        List<ProductPriceEntity> entities = findProductPrices(productId.value(), brandId.value(), validAt);

        return entities.stream()
            .map(ProductPriceMapper::toDomain)
            .toList();
    }
}
