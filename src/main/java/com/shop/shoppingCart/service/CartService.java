package com.shop.shoppingCart.service;

import com.shop.shoppingCart.dto.CartDTO;
import com.shop.shoppingCart.dto.CartItemDTO;
import com.shop.shoppingCart.model.Cart;
import com.shop.shoppingCart.model.CartItem;
import com.shop.shoppingCart.model.Product;
import com.shop.shoppingCart.model.User;
import com.shop.shoppingCart.repository.CartItemRepository;
import com.shop.shoppingCart.repository.CartRepository;
import com.shop.shoppingCart.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CouponService couponService;

    public CartService(CartRepository cartRepository,
            UserRepository userRepository,
            CartItemRepository cartItemRepository,
            ProductService productService,
            CouponService couponService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.couponService = couponService;
    }

    public CartDTO getUserCart(Integer userId) {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));

        Cart cart = targetUser.getCart();
        if (cart == null) {
            throw new NoSuchElementException("User has no cart");
        }

        return convertToCartDTO(cart);
    }

    public CartDTO addItemToCart(CartItem cartItem, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = user.getCart();

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
        }

        Product product = productService.getProductById(cartItem.getProductId());
        if (product == null) {
            throw new NoSuchElementException("Product not found with ID: " + cartItem.getProductId());
        }

        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);
        cartItemRepository.save(cartItem);

        return convertToCartDTO(cartRepository.save(cart));
    }

    public CartDTO removeItemFromCart(Integer userId, Integer cartItemId) {
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

        return convertToCartDTO(cartRepository.save(userCart));
    }

    public CartDTO applyCoupon(Integer userId, String couponCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new IllegalStateException("User has no cart");
        }

        CartDTO cartDTO = convertToCartDTO(cart);

        // Apply coupon discount
        double discountedAmount = couponService.applyDiscount(
                cartDTO.getTotalAmount(),
                couponCode);

        double discount = cartDTO.getTotalAmount() - discountedAmount;
        cartDTO.setDiscountAmount(discount);
        cartDTO.setAppliedCoupon(couponCode);

        return cartDTO;
    }

    private CartDTO convertToCartDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getCartItems().stream()
                .map(item -> {
                    Product product = productService.getProductById(item.getProductId());
                    if (product == null) {
                        // If product not found, create a minimal product with available info
                        return new CartItemDTO(
                                item.getId(),
                                item.getProductId(),
                                "Product not found (ID: " + item.getProductId() + ")",
                                0.0,
                                item.getQuantity());
                    }
                    return new CartItemDTO(
                            item.getId(),
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            item.getQuantity());
                })
                .collect(Collectors.toList());

        double totalAmount = itemDTOs.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        return new CartDTO(
                cart.getId(),
                cart.getUser() != null ? cart.getUser().getUsername() : "Unknown User",
                itemDTOs,
                totalAmount);
    }
}
