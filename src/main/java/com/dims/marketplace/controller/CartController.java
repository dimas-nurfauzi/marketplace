package com.dims.marketplace.controller;

import com.dims.marketplace.dto.ApiResponse;
import com.dims.marketplace.dto.cart.response.CartResponse;
import com.dims.marketplace.dto.cart.update.UpdateCartItemRequest;
import com.dims.marketplace.service.inter.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<ApiResponse<Void>> addToCart(
            @RequestParam UUID variantId,
            @RequestParam Integer quantity,
            Authentication authentication) {

        String email = authentication.getName();

        cartService.addToCart(email, variantId, quantity);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Item added to cart", null)
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            Authentication authentication) {

        String email = authentication.getName();

        CartResponse response = cartService.getCart(email);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success", response)
        );
    }

    @PatchMapping("/items/{id}")
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<ApiResponse<Void>> updateQuantity(
            @PathVariable UUID id,
            @RequestBody UpdateCartItemRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        cartService.updateQuantity(id, request.getQuantity(), email);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cart updated", null)
        );
    }

    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<ApiResponse<Void>> removeItem(
            @PathVariable UUID id,
            Authentication authentication) {

        String email = authentication.getName();

        cartService.removeItem(id, email);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Item removed", null)
        );
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<ApiResponse<Void>> clearCart(
            Authentication authentication) {

        String email = authentication.getName();

        cartService.clearCart(email);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cart cleared", null)
        );
    }
}