package com.example.truststock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.truststock.R;
import com.example.truststock.adapter.ProductAdapter;
import com.example.truststock.manager.CartManager;
import com.example.truststock.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> products;
    private FirebaseFirestore db;
    ImageButton btnProfile;
    Button btnSearch;
    FloatingActionButton btnCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        recyclerView = findViewById(R.id.recyclerProducts);
        btnCart = findViewById(R.id.btnCart);
        btnProfile = findViewById(R.id.btnProfile);
        btnSearch = findViewById(R.id.btnSearch);
        EditText etSearch = findViewById(R.id.etSearch);

        Button btnComment = findViewById(R.id.btnComment);

        btnComment.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerCommentsActivity.class);
            startActivity(intent);
        });



        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            adapter.filter(query);
        });

        products = new ArrayList<>();
        adapter = new ProductAdapter(products, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadProducts();
        loadAverageRatings();

        btnSearch.setOnClickListener(v ->
                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
        );

        btnCart.setOnClickListener(v -> {
            if (CartManager.getCart().isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, CartActivity.class));
            }
        });


    }

    private void loadProducts() {
        db.collection("products")
                .whereEqualTo("status", "active")
                .get()
                .addOnSuccessListener(snapshot -> {
                    products.clear();
                    for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot) {
                        Product p = doc.toObject(Product.class);
                        if (p != null) {
                            p.setId(doc.getId());
                            products.add(p);
                        }
                    }
                    adapter.setOriginalList(products);
                    adapter.notifyDataSetChanged();
                });
    }
    private Map<String, Float> ratingMap = new HashMap<>();
    private void loadAverageRatings() {
        FirebaseFirestore.getInstance()
                .collection("comments")
                .get()
                .addOnSuccessListener(snapshot -> {

                    Map<String, Integer> countMap = new HashMap<>();
                    Map<String, Integer> sumMap = new HashMap<>();

                    for (var doc : snapshot.getDocuments()) {
                        String product = doc.getString("productName");
                        Long rating = doc.getLong("rating");

                        if (product == null || rating == null) continue;

                        sumMap.put(product, sumMap.getOrDefault(product, 0) + rating.intValue());
                        countMap.put(product, countMap.getOrDefault(product, 0) + 1);
                    }

                    ratingMap.clear();
                    for (String product : sumMap.keySet()) {
                        float avg = (float) sumMap.get(product) / countMap.get(product);
                        ratingMap.put(product, avg);
                    }

                    adapter.setRatingMap(ratingMap);
                });
    }

}

