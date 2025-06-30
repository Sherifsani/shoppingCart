package com.shop.shoppingCart.controller;

import com.shop.shoppingCart.dto.CartDTO;
import com.shop.shoppingCart.model.CartItem;
import com.shop.shoppingCart.service.CartItemService;
import com.shop.shoppingCart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;

    public CartController(CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Integer userId) {
        CartDTO cart = cartService.getUserCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartDTO> addItemToCart(@RequestBody CartItem cartItem, @PathVariable Integer userId) {
        CartDTO updatedCart = cartService.addItemToCart(cartItem, userId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/remove/{cartItemId}")
    public ResponseEntity<CartDTO> removeItemFromCart(
            @PathVariable Integer userId,
            @PathVariable Integer cartItemId) {
        CartDTO updatedCart = cartService.removeItemFromCart(userId, cartItemId);
        return ResponseEntity.ok(updatedCart);
    }

    @PatchMapping("/{cartItemId}/quantity")
    public ResponseEntity<CartItem> updateQuantity(
            @PathVariable Integer cartItemId,
            @RequestParam int quantity) {
        CartItem updated = cartItemService.updateQuantity(cartItemId, quantity);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{cartItemId}/increment")
    public ResponseEntity<CartItem> incrementQuantity(@PathVariable Integer cartItemId) {
        CartItem updated = cartItemService.incrementQuantity(cartItemId);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{cartItemId}/decrement")
    public ResponseEntity<CartItem> decrementQuantity(@PathVariable Integer cartItemId) {
        CartItem updated = cartItemService.decrementQuantity(cartItemId);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{userId}/apply-coupon/{couponCode}")
    public ResponseEntity<CartDTO> applyCoupon(
            @PathVariable Integer userId,
            @PathVariable String couponCode) {
        CartDTO updatedCart = cartService.applyCoupon(userId, couponCode);
        return ResponseEntity.ok(updatedCart);
    }
}
