package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Récupérer toutes les commandes
     */
    public List<Order> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll();
    }

    /**
     * Récupérer une commande par ID
     */
    public Order getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);

        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with ID: " + id));
    }

    /**
     * Créer une nouvelle commande
     */
    public Order createOrder(Order order) {
        log.info("Creating new order for product ID: {}", order.getProductId());

        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Order quantity must be greater than 0");
        }

        if (order.getTotalPrice() <= 0) {
            throw new IllegalArgumentException("Order total price must be greater than 0");
        }

        return orderRepository.save(order);
    }

    /**
     * Mettre à jour une commande
     */
    public Order updateOrder(Long id, Order orderDetails) {
        log.info("Updating order with ID: {}", id);

        Order order = getOrderById(id);

        if (orderDetails.getQuantity() != null && orderDetails.getQuantity() > 0) {
            order.setQuantity(orderDetails.getQuantity());
        }
        if (orderDetails.getTotalPrice() != null && orderDetails.getTotalPrice() > 0) {
            order.setTotalPrice(orderDetails.getTotalPrice());
        }
        if (orderDetails.getStatus() != null) {
            order.setStatus(orderDetails.getStatus());
        }

        return orderRepository.save(order);
    }

    /**
     * Annuler une commande
     */
    public Order cancelOrder(Long id) {
        log.info("Cancelling order with ID: {}", id);

        Order order = getOrderById(id);

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new IllegalArgumentException(
                    "Cannot cancel a delivered order");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    /**
     * Confirmer une commande
     */
    public Order confirmOrder(Long id) {
        log.info("Confirming order with ID: {}", id);

        Order order = getOrderById(id);
        order.setStatus(Order.OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }

    /**
     * Récupérer les commandes par product ID
     */
    public List<Order> getOrdersByProductId(Long productId) {
        log.info("Fetching orders for product ID: {}", productId);
        return orderRepository.findByProductId(productId);
    }

    /**
     * Supprimer une commande
     */
    public void deleteOrder(Long id) {
        log.info("Deleting order with ID: {}", id);

        Order order = getOrderById(id);
        orderRepository.delete(order);
    }
}