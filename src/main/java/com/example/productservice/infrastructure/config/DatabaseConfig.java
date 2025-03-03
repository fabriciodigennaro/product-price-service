package com.example.productservice.infrastructure.config;

import com.example.productservice.domain.productprice.ProductPriceRepository;
import com.example.productservice.infrastructure.database.JpaProductPriceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaRepositories(basePackages = "com.example.productservice.infrastructure.database")
public class DatabaseConfig {

    private final JpaProductPriceRepository jpaProductPriceRepository;

    public DatabaseConfig(JpaProductPriceRepository jpaProductPriceRepository) {
        this.jpaProductPriceRepository = jpaProductPriceRepository;
    }

    @Bean
    public ProductPriceRepository productPriceRepository() {
        return jpaProductPriceRepository;
    }
}
