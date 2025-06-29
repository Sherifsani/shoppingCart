package com.shop.shoppingCart.repository;

import com.shop.shoppingCart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
