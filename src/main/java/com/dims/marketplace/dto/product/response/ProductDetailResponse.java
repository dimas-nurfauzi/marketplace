package com.dims.marketplace.dto.product.response;

import com.dims.marketplace.dto.enums.Category;
import com.dims.marketplace.dto.product.variant.VariantResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductDetailResponse {
    private UUID id;
    private UUID userId;
    private String name;
    private String description;
    private Category category;
    private LocalDateTime createdAt;
    private List<VariantResponse> variants;
}