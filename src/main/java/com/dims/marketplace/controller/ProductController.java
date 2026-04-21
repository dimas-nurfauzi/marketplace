package com.dims.marketplace.controller;

import com.dims.marketplace.dto.ApiResponse;
import com.dims.marketplace.dto.enums.Category;
import com.dims.marketplace.dto.mapper.ProductMapper;
import com.dims.marketplace.dto.product.create.ProductRequest;
import com.dims.marketplace.dto.product.response.ProductDetailResponse;
import com.dims.marketplace.dto.product.response.ProductListResponse;
import com.dims.marketplace.dto.product.response.ProductResponse;
import com.dims.marketplace.dto.product.update.UpdateProductRequest;
import com.dims.marketplace.entity.Product;
import com.dims.marketplace.service.inter.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(
            @RequestBody ProductRequest request) {

        Product product = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        201,
                        "Product created successfully",
                        ProductMapper.toResponse(product)
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductListResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID sellerId
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductListResponse> data = productService.getAllProducts(
                minPrice,
                maxPrice,
                category,
                name,
                sellerId,
                pageable
        );

        return ResponseEntity.ok(new ApiResponse<>(200, "Success", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getById(@PathVariable UUID id) {

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success", productService.getProductById(id))
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable UUID id,
            @RequestBody UpdateProductRequest request) {

        Product product = productService.updateProduct(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Product updated successfully",
                        ProductMapper.toResponse(product))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Product deleted successfully", null)
        );
    }
}