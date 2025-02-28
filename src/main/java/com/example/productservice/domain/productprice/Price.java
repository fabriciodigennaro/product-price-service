package com.example.productservice.domain.productprice;

import javax.money.CurrencyUnit;
import java.math.BigDecimal;

public record Price(BigDecimal amount, CurrencyUnit currency) {}
