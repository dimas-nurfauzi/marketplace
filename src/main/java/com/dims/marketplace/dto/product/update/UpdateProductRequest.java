package com.dims.marketplace.dto.product.update;

import com.dims.marketplace.dto.enums.Category;
import com.dims.marketplace.dto.product.create.VariantRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    private UUID productId;
    private String name;
    private String description;
    private Category category;
    private List<VariantRequest> variants;
}