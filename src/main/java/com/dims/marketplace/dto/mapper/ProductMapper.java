package com.dims.marketplace.dto.mapper;

import com.dims.marketplace.dto.product.response.*;
import com.dims.marketplace.dto.product.variant.VariantResponse;
import com.dims.marketplace.entity.Product;
import com.dims.marketplace.entity.Variant;

import java.math.BigDecimal;
import java.util.List;

public class ProductMapper {

    public static ProductResponse toResponse(Product product) {

        List<VariantResponse> variants = product.getVariants()
                .stream()
                .map(ProductMapper::toVariantResponse)
                .toList();

        BigDecimal minPrice = variants.stream()
                .map(VariantResponse::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return new ProductResponse(
                product.getId(),
                product.getSeller().getId(),
                product.getName(),
                product.getDescription(),
                minPrice,
                product.getCategory(),
                product.getCreatedAt(),
                variants
        );
    }

    public static ProductListResponse toListResponse(Product product) {

        BigDecimal minPrice = product.getVariants().stream()
                .map(Variant::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return new ProductListResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                minPrice,
                product.getCategory(),
                product.getCreatedAt()
        );
    }

    private static VariantResponse toVariantResponse(Variant v) {
        return new VariantResponse(
                v.getId(),
                v.getSku(),
                v.getSize(),
                v.getPrice(),
                v.getStock(),
                v.getCreatedAt()
        );
    }
}