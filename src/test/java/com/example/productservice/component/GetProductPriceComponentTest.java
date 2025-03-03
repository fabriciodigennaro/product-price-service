package com.example.productservice.component;

import com.example.productservice.domain.productprice.BrandId;
import com.example.productservice.domain.productprice.Price;
import com.example.productservice.domain.productprice.ProductId;
import com.example.productservice.domain.productprice.ProductPrice;
import com.example.productservice.infrastructure.database.entity.ProductPriceEntity;
import com.example.productservice.infrastructure.entrypoint.rest.response.ProductPriceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import jakarta.persistence.EntityManager;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
class GetProductPriceComponentTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    private static final BrandId brandId = new BrandId(1);
    private static final ProductId productId = new ProductId(35455);
    private static final List<ProductPrice> givenDataset = List.of(
        new ProductPrice(
            brandId,
            LocalDateTime.parse("2020-06-14T00:00:00"),
            LocalDateTime.parse("2020-12-31T23:59:59"),
            1,
            productId,
            0,
            new Price(new BigDecimal("35.50"), Monetary.getCurrency("EUR"))
        ),
        new ProductPrice(
            brandId,
            LocalDateTime.parse("2020-06-14T15:00:00"),
            LocalDateTime.parse("2020-06-14T18:30:00"),
            2,
            productId,
            1,
            new Price(new BigDecimal("25.45"), Monetary.getCurrency("EUR"))
        ),
        new ProductPrice(
            brandId,
            LocalDateTime.parse("2020-06-15T00:00:00"),
            LocalDateTime.parse("2020-06-15T11:00:00"),
            3,
            productId,
            1,
            new Price(new BigDecimal("30.50"), Monetary.getCurrency("EUR"))
        ),
        new ProductPrice(
            brandId,
            LocalDateTime.parse("2020-06-15T16:00:00"),
            LocalDateTime.parse("2020-12-31T23:59:59"),
            4,
            productId,
            1,
            new Price(new BigDecimal("38.95"), Monetary.getCurrency("EUR"))
        )
    );

    @Test
    void getProductPrice() throws JsonProcessingException {
        // Given
        LocalDateTime validAt = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        ProductPrice productPrice = new ProductPrice(
            brandId,
            validAt.minusDays(1),
            validAt.plusDays(1),
            1,
            productId,
            1,
            new Price(new BigDecimal("9.99"), Monetary.getCurrency("EUR"))
        );
        ProductPriceResponse productPriceResponse = new ProductPriceResponse(
            productPrice.productId().value(),
            productPrice.brandId().value(),
            productPrice.priceList(),
            productPrice.startDate(),
            productPrice.endDate(),
            productPrice.price().amount(),
            productPrice.price().currency().getCurrencyCode()
        );
        givenExistingProductPrice(productPrice);
        String expectedJson = objectMapper.writeValueAsString(productPriceResponse);

        // When
        MockMvcResponse response = whenARequestToGetAProductPriceIsReceived(
            productPrice.productId(),
            productPrice.brandId(),
            validAt
        );

        // Then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body(CoreMatchers.equalTo(expectedJson));
    }

    /*
    I have included these parameterized tests to cover the test cases requested in the example description.
    This decision was made because the example description implied that these test cases should be included as part
    of the component (E2E) tests.
    However, these scenarios have also been covered by other types of tests.
    In a typical production project, I would aim to avoid having component tests for scenarios that could be covered
    by more cost-effective tests.
     */
    @ParameterizedTest
    @MethodSource("testScenariosWithGivenDataset")
    void shouldReturnValidPriceAtGivenDate(
        LocalDateTime validAt,
        ProductPrice expectedProductPrice
    ) throws JsonProcessingException {
        // Given
        givenExistingDataset();
        ProductPriceResponse productPriceResponse = new ProductPriceResponse(
            expectedProductPrice.productId().value(),
            expectedProductPrice.brandId().value(),
            expectedProductPrice.priceList(),
            expectedProductPrice.startDate(),
            expectedProductPrice.endDate(),
            expectedProductPrice.price().amount(),
            expectedProductPrice.price().currency().getCurrencyCode()
        );
        String expectedJson = objectMapper.writeValueAsString(productPriceResponse);

        // When
        MockMvcResponse response = whenARequestToGetAProductPriceIsReceived(
            expectedProductPrice.productId(),
            expectedProductPrice.brandId(),
            validAt
        );

        // Then
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body(CoreMatchers.equalTo(expectedJson));
    }

    private static List<Arguments> testScenariosWithGivenDataset() {
        return List.of(
            Arguments.of(LocalDateTime.of(2020, 6, 14, 10, 0), givenDataset.get(0)),
            Arguments.of(LocalDateTime.of(2020, 6, 14, 16, 0), givenDataset.get(1)),
            Arguments.of(LocalDateTime.of(2020, 6, 14, 21, 0), givenDataset.get(0)),
            Arguments.of(LocalDateTime.of(2020, 6, 15, 10, 0), givenDataset.get(2)),
            Arguments.of(LocalDateTime.of(2020, 6, 16, 21, 0), givenDataset.get(3))
        );
    }

    private void givenExistingDataset() {
        givenDataset.forEach(this::givenExistingProductPrice);
    }

    private void givenExistingProductPrice(ProductPrice productPrice) {
        ProductPriceEntity entity = new ProductPriceEntity();
        entity.setId(UUID.randomUUID());
        entity.setBrandId(productPrice.brandId().value());
        entity.setStartDate(productPrice.startDate());
        entity.setEndDate(productPrice.endDate());
        entity.setPriceList(productPrice.priceList());
        entity.setProductId(productPrice.productId().value());
        entity.setPriority(productPrice.priority());
        entity.setPrice(productPrice.price().amount());
        entity.setCurrency(productPrice.price().currency().getCurrencyCode());
        entityManager.persist(entity);
    }

    private MockMvcResponse whenARequestToGetAProductPriceIsReceived(
        ProductId productId,
        BrandId brandId,
        LocalDateTime validAt
    ) {
        return RestAssuredMockMvc
            .given()
            .webAppContextSetup(context)
            .contentType(ContentType.JSON)
            .param("productId", productId.value())
            .param("brandId", brandId.value())
            .param("validAt", validAt.toString())
            .when()
            .get("/prices");
    }
}
