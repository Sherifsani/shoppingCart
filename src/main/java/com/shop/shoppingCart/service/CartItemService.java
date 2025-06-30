package com.shop.shoppingCart.service;

import com.shop.shoppingCart.model.CartItem;
import com.shop.shoppingCart.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartItemService(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    public CartItem updateQuantity(Integer cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found"));
        ;
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public CartItem incrementQuantity(Integer cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found with ID: " + cartItemId));

        // Refresh the product details to ensure we have the latest price
        var product = productService.getProductById(cartItem.getProductId());
        if (product == null) {
            throw new IllegalStateException("Product not found for cart item");
        }

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItem.setPrice(product.getPrice()); // Ensure price is up to date
        cartItem.setProductName(product.getName()); // Ensure product name is up to date

        return cartItemRepository.save(cartItem);
    }

    public CartItem decrementQuantity(Integer cartItemId) {
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
