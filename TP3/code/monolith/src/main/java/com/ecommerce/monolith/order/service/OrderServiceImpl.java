package com.ecommerce.monolith.order.service;

import com.ecommerce.monolith.customer.service.CustomerService;
import com.ecommerce.monolith.order.dto.CreateOrderRequest;
import com.ecommerce.monolith.order.dto.OrderDTO;
import com.ecommerce.monolith.order.dto.OrderItemDTO;
import com.ecommerce.monolith.order.mapper.OrderMapper;
import com.ecommerce.monolith.order.model.Order;
import com.ecommerce.monolith.order.model.OrderItem;
import com.ecommerce.monolith.order.repository.OrderRepository;
import com.ecommerce.monolith.product.dto.ProductDTO;
import com.ecommerce.monolith.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductService productService;
    private final CustomerService customerService;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        log.info("Fetching all orders");
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::enrichOrderDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        log.info("Fetching order with id: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Order not found with id: " + id));
        return enrichOrderDTO(order);
    }

    @Override
    public OrderDTO createOrder(CreateOrderRequest request) {
        log.info("Creating new order for customer: {}", request.getCustomerId());

        // Vérifier que le customer existe
        customerService.getCustomerById(request.getCustomerId());

        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var itemRequest : request.getItems()) {
            ProductDTO product = productService.getProductById(itemRequest.getProductId());

            BigDecimal subTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .productId(itemRequest.getProductId())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(product.getPrice())
                    .subTotal(subTotal)
                    .build();

            order.getItems().add(item);
            totalAmount = totalAmount.add(subTotal);
        }

        order.setTotalAmount(totalAmount);
        Order saved = orderRepository.save(order);

        return enrichOrderDTO(saved);
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("Deleting order with id: {}", id);
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    private OrderDTO enrichOrderDTO(Order order) {
        OrderDTO dto = orderMapper.toDTO(order);

        try {
            dto.setCustomerName(
                    customerService.getCustomerById(order.getCustomerId()).getName());
        } catch (Exception e) {
            dto.setCustomerName("Unknown Customer");
        }

        if (dto.getItems() != null) {
            for (OrderItemDTO itemDTO : dto.getItems()) {
                try {
                    itemDTO.setProductName(
                            productService.getProductById(itemDTO.getProductId()).getName());
                } catch (Exception e) {
                    itemDTO.setProductName("Unknown Product");
                }
            }
        }

        return dto;
    }
}