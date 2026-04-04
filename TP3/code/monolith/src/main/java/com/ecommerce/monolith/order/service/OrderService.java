package com.ecommerce.monolith.order.service;

import com.ecommerce.monolith.order.dto.CreateOrderRequest;
import com.ecommerce.monolith.order.dto.OrderDTO;
import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(Long id);

    OrderDTO createOrder(CreateOrderRequest request);

    void deleteOrder(Long id);
}