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

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       CartItemRepository cartItemRepository,
                       ProductService productService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
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

    private CartDTO convertToCartDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getCartItems().stream().map(item -> {
            Product product = productService.getProductById(item.getProductId());
            return new CartItemDTO(
                    item.getId(),
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    item.getQuantity()
            );
        }).collect(Collectors.toList());

        double totalAmount = itemDTOs.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        return new CartDTO(itemDTOs, totalAmount);
    }
}
