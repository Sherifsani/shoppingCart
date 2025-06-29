package com.shop.shoppingCart.service;

import com.shop.shoppingCart.model.CartItem;
import com.shop.shoppingCart.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository){
        this.cartItemRepository = cartItemRepository;
    }

    public CartItem updateQuantity(Integer cartItemId, int quantity){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found"));
        ;
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    public CartItem incrementQuantity(Integer cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found"));
        ;
        cartItem.setQuantity(cartItem.getQuantity() + 1);
        return cartItemRepository.save(cartItem);
    }

    public CartItem decrementQuantity(Integer cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found"));
        ;
        int currentQty = cartItem.getQuantity();
        if (currentQty <= 1) {
            throw new IllegalStateException("Quantity cannot be less than 1");
        }

        cartItem.setQuantity(currentQty - 1);
        return cartItemRepository.save(cartItem);
    }
}
