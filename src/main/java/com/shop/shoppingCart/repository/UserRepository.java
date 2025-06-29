package com.shop.shoppingCart.repository;

import com.shop.shoppingCart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
