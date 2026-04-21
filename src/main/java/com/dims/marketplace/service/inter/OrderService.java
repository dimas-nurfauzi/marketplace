package com.dims.marketplace.service.inter;

import com.dims.marketplace.dto.cart.response.CheckoutResponse;
import com.dims.marketplace.dto.order.response.OrderResponse;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    CheckoutResponse placeOrder(UUID userId);

    @Transactional(readOnly = true)
    List<OrderResponse> getUserOrders(UUID userId);

    @Transactional(readOnly = true)
    OrderResponse getOrderDetail(UUID orderId);
}