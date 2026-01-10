package com.example.truststock.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truststock.R;
import com.example.truststock.adapter.LowStockAdapter;
import com.example.truststock.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LowStockActivity extends AppCompatActivity {

    private RecyclerView recyclerLowStock;
    private LowStockAdapter adapter;
    private List<Product> lowStockProducts;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_stock);

        recyclerLowStock = findViewById(R.id.recyclerLowStock);
        lowStockProducts = new ArrayList<>();
        adapter = new LowStockAdapter(lowStockProducts, this);
        recyclerLowStock.setLayoutManager(new LinearLayoutManager(this));
        recyclerLowStock.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadLowStockProducts();
    }

    private void loadLowStockProducts() {
        db.collection("products").whereLessThanOrEqualTo("stock", 5)
                .get().addOnSuccessListener(snapshot -> {
                    lowStockProducts.clear();
                    for (var doc : snapshot.getDocuments()) {
                        Product p = doc.toObject(Product.class);
                        p.setId(doc.getId());
                        lowStockProducts.add(p);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}


