package com.dims.marketplace.controller;

import com.dims.marketplace.dto.ApiResponse;
import com.dims.marketplace.dto.product.create.VariantRequest;
import com.dims.marketplace.dto.product.variant.UpdateVariantRequest;
import com.dims.marketplace.dto.product.variant.VariantResponse;
import com.dims.marketplace.entity.Variant;
import com.dims.marketplace.service.inter.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/variants")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<VariantResponse>> updateVariant(
            @PathVariable UUID id,
            @RequestBody UpdateVariantRequest request
    ) {

        Variant variant = variantService.updateVariant(id, request);

        VariantResponse response = mapToResponse(variant);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Variant updated successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVariant(@PathVariable UUID id) {

        variantService.deleteVariant(id);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Variant deleted successfully", null)
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<VariantResponse>>> getByProduct(
            @PathVariable UUID productId
    ) {

        List<VariantResponse> response = variantService.getVariantsByProduct(productId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success", response)
        );
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<VariantResponse>> addVariant(
            @PathVariable UUID productId,
            @RequestBody VariantRequest request,
            Authentication authentication
    ) {

        Variant variant = variantService.addVariantToProduct(productId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Variant created", mapToResponse(variant)));
    }

    private VariantResponse mapToResponse(Variant v) {
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