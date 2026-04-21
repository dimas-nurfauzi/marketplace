package com.dims.marketplace.service.inter;

import com.dims.marketplace.dto.cart.response.CartResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CartService {
    void addToCart(UUID userId, UUID variantId, Integer quantity);

    @Transactional(readOnly = true)
    CartResponse getCart(UUID userId);

    void updateQuantity(UUID cartItemId, Integer quantity);

    void removeItem(UUID cartItemId);

    void clearCart(UUID userId);
}