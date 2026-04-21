package com.dims.marketplace.dto.cart.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartItemRequest {
    private UUID id;
    private UUID cartId;
    private UUID productVariantId;
    private UUID quantityId;
    private Integer quantity;
}
