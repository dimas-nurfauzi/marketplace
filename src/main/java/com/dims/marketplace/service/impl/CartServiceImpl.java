package com.dims.marketplace.service.impl;

import com.dims.marketplace.dto.cart.response.CartItemResponse;
import com.dims.marketplace.dto.cart.response.CartResponse;
import com.dims.marketplace.entity.Cart;
import com.dims.marketplace.entity.CartItem;
import com.dims.marketplace.entity.Variant;
import com.dims.marketplace.entity.User;
import com.dims.marketplace.exceptions.BadRequestException;
import com.dims.marketplace.exceptions.DataNotFoundException;
import com.dims.marketplace.exceptions.UnauthorizedException;
import com.dims.marketplace.repository.CartItemRepository;
import com.dims.marketplace.repository.CartRepository;
import com.dims.marketplace.repository.UserRepository;
import com.dims.marketplace.repository.VariantRepository;
import com.dims.marketplace.service.inter.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final VariantRepository variantRepository;

    @Override
    public void addToCart(String email, UUID variantId, Integer quantity) {

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Variant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new DataNotFoundException("Variant not found"));

        if (quantity > variant.getStock()) {
            throw new BadRequestException("Stock not enough");
        }

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getVariant().getId().equals(variantId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();

            int newQty = item.getQuantity() + quantity;

            if (newQty > variant.getStock()) {
                throw new BadRequestException("Stock not enough");
            }

            item.setQuantity(newQty);

        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setVariant(variant);
            newItem.setQuantity(quantity);

            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    @Override
    public CartResponse getCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new DataNotFoundException("Cart not found"));

        List<CartItemResponse> itemResponses = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {

            Variant variant = item.getVariant();

            BigDecimal price = variant.getPrice();
            Integer quantity = item.getQuantity();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));

            CartItemResponse response = new CartItemResponse(
                    item.getId(),
                    variant.getId(),
                    variant.getProduct().getName(),
                    variant.getSize(),
                    price,
                    quantity,
                    subtotal
            );

            itemResponses.add(response);
            total = total.add(subtotal);
        }

        return new CartResponse(
                cart.getId(),
                user.getId(),
                itemResponses,
                total
        );
    }

    @Override
    public void updateQuantity(UUID cartItemId, Integer quantity, String email) {

        if (quantity < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new DataNotFoundException("Cart item not found"));

        // 🔥 VALIDASI OWNER
        if (!item.getCart().getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You don't have access to this cart item");
        }

        Variant variant = item.getVariant();

        if (quantity == 0) {
            item.getCart().getItems().remove(item);
            cartItemRepository.delete(item);
            return;
        }

        if (quantity > variant.getStock()) {
            throw new BadRequestException("Stock not enough");
        }

        item.setQuantity(quantity);
    }

    @Override
    public void removeItem(UUID cartItemId, String email) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new DataNotFoundException("Cart item not found"));

        // 🔥 VALIDASI OWNER
        if (!item.getCart().getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You don't have access to this cart item");
        }

        item.getCart().getItems().remove(item);
        cartItemRepository.delete(item);
    }

    @Override
    public void clearCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new DataNotFoundException("Cart not found"));

        cart.getItems().clear();
    }
}