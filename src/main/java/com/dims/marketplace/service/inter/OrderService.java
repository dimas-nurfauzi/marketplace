package com.dims.marketplace.service.inter;

import com.dims.marketplace.dto.cart.response.CheckoutResponse;
import com.dims.marketplace.dto.order.response.OrderResponse;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    CheckoutResponse placeOrder(String email);

    List<OrderResponse> getUserOrders(String email);

    OrderResponse getOrderDetail(UUID orderId, String email);
}