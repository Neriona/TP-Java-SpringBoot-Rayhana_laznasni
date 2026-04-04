package com.ecommerce.monolith.order.mapper;

import com.ecommerce.monolith.order.dto.OrderDTO;
import com.ecommerce.monolith.order.dto.OrderItemDTO;
import com.ecommerce.monolith.order.model.Order;
import com.ecommerce.monolith.order.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "customerName", ignore = true)
    @Mapping(target = "items", source = "items")
    OrderDTO toDTO(Order order);

    @Mapping(target = "productName", ignore = true)
    OrderItemDTO toItemDTO(OrderItem item);

    List<OrderDTO> toDTOList(List<Order> orders);
}