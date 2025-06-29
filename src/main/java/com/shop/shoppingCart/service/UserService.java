package com.shop.shoppingCart.service;

import com.shop.shoppingCart.model.Cart;
import com.shop.shoppingCart.model.User;
import com.shop.shoppingCart.repository.CartRepository;
import com.shop.shoppingCart.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public UserService(UserRepository userRepository, CartRepository cartRepository){
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getOneUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));
    }

    public User createUser(User user) {
        User savedUser = userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(savedUser);
        Cart savedCart = cartRepository.save(cart);

        savedUser.setCart(savedCart);
        return userRepository.save(savedUser);
    }

    public User updateUser(Integer userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(" Cannot update: User with ID " + userId + " not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setCart(updatedUser.getCart());

        return userRepository.save(existingUser);
    }


    public String deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(" Cannot delete: User with ID " + userId + " does not exist");
        }

        userRepository.deleteById(userId);
        return "✅ User with ID " + userId + " deleted successfully.";
    }
}
