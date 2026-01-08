package com.example.truststock.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truststock.R;
import com.example.truststock.adapter.CartAdapter;
import com.example.truststock.manager.CartManager;
import com.example.truststock.model.CartItem;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;


public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private CartAdapter adapter;
    private EditText etName, etAddress, etPhone;
    private TextView tvTotal;
    private Button btnPlaceOrder;
    private List<CartItem> cartList;
    private FirebaseFirestore db;
    private double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCart = findViewById(R.id.recyclerCart);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        tvTotal = findViewById(R.id.tvTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        db = FirebaseFirestore.getInstance();
        cartList = CartManager.getCart();

        adapter = new CartAdapter(cartList, this);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(adapter);

        total = 0;
        for (CartItem item : cartList) total += item.getTotalPrice();
        tvTotal.setText("Total: $" + total);

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }


    private void placeOrder() {
        String name = etName.getText().toString();
        String address = etAddress.getText().toString();
        String phone = etPhone.getText().toString();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<CartItem> itemsToBuy = new ArrayList<>();
        List<String> outOfStockItems = new ArrayList<>();

        for (CartItem item : cartList) {
            if (item.getQuantity() > item.getProduct().getStock()) {
                outOfStockItems.add(item.getProduct().getName());
            } else {
                itemsToBuy.add(item);
            }
        }

        if (!outOfStockItems.isEmpty()) {
            String msg = "Cannot order due to insufficient stock: " + String.join(", ", outOfStockItems);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("customerName", name);
        orderMap.put("address", address);
        orderMap.put("phone", phone);
        orderMap.put("products", itemsToBuy);
        double total = 0;
        for (CartItem item : itemsToBuy) total += item.getTotalPrice();
        orderMap.put("total", total);
        orderMap.put("status", "pending");
        orderMap.put("timestamp", System.currentTimeMillis());


        db.collection("orders").add(orderMap)
                .addOnSuccessListener(orderRef -> {

                    for (CartItem item : itemsToBuy) {
                        String productId = item.getProduct().getId();
                        int qtyOrdered = item.getQuantity();

                        db.runTransaction(transaction -> {
                            var productRef = db.collection("products").document(productId);
                            var snapshot = transaction.get(productRef);
                            long currentStock = snapshot.getLong("stock") != null ? snapshot.getLong("stock") : 0;


                            if (currentStock < qtyOrdered) {
                                try {
                                    throw new Exception(item.getProduct().getName() + " has insufficient stock.");
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            long newStock = currentStock - qtyOrdered;
                            transaction.update(productRef, "stock", newStock);
                            return null;
                        }).addOnSuccessListener(aVoid -> {
                            Log.d("StockUpdate", "Updated stock for " + item.getProduct().getName());
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to update stock: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }

                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    cartList.clear();
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show());
    }

}
