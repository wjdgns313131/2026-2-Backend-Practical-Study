package com.gdghongik.commerce.domain.order;


import com.gdghongik.commerce.domain.product.Product;

import java.util.List;
import java.util.Optional;


public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAll();
}
