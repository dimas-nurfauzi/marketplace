package com.dims.marketplace.controller;

import com.dims.marketplace.dto.ApiResponse;
import com.dims.marketplace.dto.cart.response.CartResponse;
import com.dims.marketplace.dto.cart.update.UpdateCartItemRequest;
import com.dims.marketplace.service.inter.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<Void>> addToCart(
            @RequestParam UUID userId,
            @RequestParam UUID variantId,
            @RequestParam Integer quantity) {

        cartService.addToCart(userId, variantId, quantity);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Item added to cart", null)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @RequestParam UUID userId) {

        CartResponse response = cartService.getCart(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success", response)
        );
    }

    @PatchMapping("/items/{id}")
    public ResponseEntity<ApiResponse<Void>> updateQuantity(
            @PathVariable UUID id,
            @RequestBody UpdateCartItemRequest request) {

        cartService.updateQuantity(id, request.getQuantity());

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cart updated", null)
        );
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<ApiResponse<Void>> removeItem(@PathVariable UUID id) {

        cartService.removeItem(id);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Item removed", null)
        );
    }
}