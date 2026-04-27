package com.dims.marketplace.service.inter;

import com.dims.marketplace.dto.enums.Category;
import com.dims.marketplace.dto.product.create.ProductRequest;
import com.dims.marketplace.dto.product.response.ProductDetailResponse;
import com.dims.marketplace.dto.product.response.ProductListResponse;
import com.dims.marketplace.dto.product.update.UpdateProductRequest;
import com.dims.marketplace.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product createProduct(ProductRequest request, String authenticatedEmail);

    Product updateProduct(UUID id, UpdateProductRequest request);

    Page<ProductListResponse> getAllProducts(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Category category,
            String name,
            UUID sellerId,
            Pageable pageable
    );

    ProductDetailResponse getProductById(UUID id);

    void deleteProduct(UUID productId);
}
