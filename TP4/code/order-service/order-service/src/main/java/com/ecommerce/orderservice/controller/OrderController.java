package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Récupérer toutes les commandes
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        log.info("GET /api/orders - Fetching all orders");
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Récupérer une commande par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        log.info("GET /api/orders/{} - Fetching order by ID", id);
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Créer une nouvelle commande
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        log.info("POST /api/orders - Creating new order");
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /**
     * Mettre à jour une commande
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long id,
            @RequestBody Order orderDetails) {
        log.info("PUT /api/orders/{} - Updating order", id);
        Order updatedOrder = orderService.updateOrder(id, orderDetails);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Confirmer une commande
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable Long id) {
        log.info("POST /api/orders/{}/confirm - Confirming order", id);
        Order confirmedOrder = orderService.confirmOrder(id);
        return ResponseEntity.ok(confirmedOrder);
    }

    /**
     * Annuler une commande
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        log.info("POST /api/orders/{}/cancel - Cancelling order", id);
        Order cancelledOrder = orderService.cancelOrder(id);
        return ResponseEntity.ok(cancelledOrder);
    }

    /**
     * Récupérer les commandes par product ID
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Order>> getOrdersByProductId(@PathVariable Long productId) {
        log.info("GET /api/orders/product/{} - Fetching orders by product ID", productId);
        List<Order> orders = orderService.getOrdersByProductId(productId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Supprimer une commande
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("DELETE /api/orders/{} - Deleting order", id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}