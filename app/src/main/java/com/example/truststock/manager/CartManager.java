package com.example.truststock.manager;

import com.example.truststock.model.CartItem;
import com.example.truststock.model.Product;

import java.util.ArrayList;
import java.util.List;
public class CartManager {

    private static final List<CartItem> cart = new ArrayList<>();

    public static void addToCart(Product product, int qty) {

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {

                int newQty = item.getQuantity() + qty;

                if (newQty > product.getStock()) {
                    throw new IllegalStateException("Not enough stock");
                }

                item.setQuantity(newQty);
                return;
            }
        }

        if (qty > product.getStock()) {
            throw new IllegalStateException("Not enough stock");
        }

        cart.add(new CartItem(product, qty));
    }

    public static List<CartItem> getCart() {
        return cart;
    }

    public static void clearCart() {
        cart.clear();
    }
}


