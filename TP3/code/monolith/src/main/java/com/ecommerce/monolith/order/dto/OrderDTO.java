package com.ecommerce.monolith.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> items;
}