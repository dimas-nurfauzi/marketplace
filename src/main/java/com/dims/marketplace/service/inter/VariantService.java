package com.dims.marketplace.service.inter;

import com.dims.marketplace.dto.product.create.VariantRequest;
import com.dims.marketplace.dto.product.variant.UpdateVariantRequest;
import com.dims.marketplace.entity.Variant;

import java.util.List;
import java.util.UUID;

public interface VariantService {

    Variant updateVariant(UUID id, UpdateVariantRequest request);

    void deleteVariant(UUID id);

    List<Variant> getVariantsByProduct(UUID productId);

    Variant addVariantToProduct(UUID productId, VariantRequest request);
}