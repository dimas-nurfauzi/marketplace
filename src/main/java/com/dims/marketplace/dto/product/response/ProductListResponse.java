package com.dims.marketplace.dto.product.response;

import com.dims.marketplace.dto.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductListResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal minPrice;
    private Category category;
    private LocalDateTime createdAt;
}