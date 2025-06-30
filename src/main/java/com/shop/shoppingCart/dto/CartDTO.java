package com.shop.shoppingCart.dto;

import java.util.List;

public class CartDTO {
    private List<CartItemDTO> items;
    private double totalAmount;

    public CartDTO(List<CartItemDTO> items, double totalAmount) {
        this.items = items;
        this.totalAmount = totalAmount;
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
    }
}
