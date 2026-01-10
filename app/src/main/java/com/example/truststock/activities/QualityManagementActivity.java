package com.example.truststock.activities;

import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truststock.R;
import com.example.truststock.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.truststock.adapter.QualityAdapter;

import java.util.ArrayList;
import java.util.List;

public class QualityManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerQuality;
    private QualityAdapter adapter;
    private List<Product> products;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_management);

        recyclerQuality = findViewById(R.id.recyclerQuality);
        products = new ArrayList<>();
        adapter = new QualityAdapter(products, this);
        recyclerQuality.setLayoutManager(new LinearLayoutManager(this));
        recyclerQuality.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadProducts();
    }

    private void loadProducts() {
        db.collection("products")
                .get()
                .addOnSuccessListener(snapshot -> {
                    products.clear();

                    for (com.google.firebase.firestore.DocumentSnapshot doc : snapshot.getDocuments()) {
                        Product p = doc.toObject(Product.class);
                        if (p != null) {
                            p.setId(doc.getId());
                            products.add(p);
                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                QualityManagementActivity.this,
                                "Failed to load products: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

}


