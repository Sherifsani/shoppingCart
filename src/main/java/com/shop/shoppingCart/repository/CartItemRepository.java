package com.shop.shoppingCart.repository;

import com.shop.shoppingCart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
