package com.shop.shoppingCart.service;

import com.shop.shoppingCart.model.Cart;
import com.shop.shoppingCart.model.CartItem;
import com.shop.shoppingCart.model.User;
import com.shop.shoppingCart.repository.CartItemRepository;
import com.shop.shoppingCart.repository.CartRepository;
import com.shop.shoppingCart.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Cart getUserCart(Integer userId) {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));

        Cart cart = targetUser.getCart();
        if (cart == null) {
            throw new NoSuchElementException("User has no cart");
        }
        return cart;
    }

    public Cart addItemToCart(CartItem cartItem, Integer userId) {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));

        Cart userCart = targetUser.getCart();
        if (userCart == null) {
            throw new IllegalStateException("User has no cart");
        }
        userCart.addCartItem(cartItem);

        cartItemRepository.save(cartItem);
        return cartRepository.save(userCart);
    }

    public Cart removeItemFromCart(Integer userId, Integer cartItemId) {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));

        Cart userCart = targetUser.getCart();
        if (userCart == null) {
            throw new IllegalStateException("User has no cart");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item with ID " + cartItemId + " not found"));

        if (!cartItem.getCart().getId().equals(userCart.getId())) {
            throw new IllegalArgumentException("This cart item does not belong to the user's cart");
        }

        userCart.removeCartItem(cartItem);

        cartItemRepository.delete(cartItem);

        return cartRepository.save(userCart);
    }
}
