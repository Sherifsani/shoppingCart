package com.shop.shoppingCart.dto;

import java.util.List;

public class CartDTO {
    private Integer id;
    private String username;
    private List<CartItemDTO> items;
    private double totalAmount;
    private double discountAmount = 0.0;
    private double finalAmount;
    private String appliedCoupon;

    public CartDTO() {
    }

    public CartDTO(Integer id, String username, List<CartItemDTO> items, double totalAmount) {
        this.id = id;
        this.username = username;
        this.items = items;
        this.totalAmount = totalAmount;
        this.finalAmount = totalAmount - discountAmount;
    }

    public CartDTO(List<CartItemDTO> items, double totalAmount) {
        this.items = items;
        this.totalAmount = totalAmount;
        this.finalAmount = totalAmount - discountAmount;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        this.finalAmount = totalAmount - discountAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
        this.finalAmount = totalAmount - discountAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public String getAppliedCoupon() {
        return appliedCoupon;
    }

    public void setAppliedCoupon(String appliedCoupon) {
        this.appliedCoupon = appliedCoupon;
    }
}
