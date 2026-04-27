package com.dims.marketplace.service.inter;

import com.dims.marketplace.dto.cart.response.CartResponse;

import java.util.UUID;

public interface CartService {

    void addToCart(String email, UUID variantId, Integer quantity);

    CartResponse getCart(String email);

    void updateQuantity(UUID cartItemId, Integer quantity, String email);

    void removeItem(UUID cartItemId, String email);

    void clearCart(String email);
}