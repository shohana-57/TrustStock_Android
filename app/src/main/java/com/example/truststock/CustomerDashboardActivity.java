package com.example.truststock;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truststock.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class CustomerDashboardActivity extends AppCompatActivity {

    private EditText etSearch;
    private RecyclerView recyclerProducts;

    private FirebaseFirestore db;

    private ArrayList<Product> productList;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        TextView tvCustomerName = findViewById(R.id.tvCustomerName);
        ImageButton btnProfile = findViewById(R.id.btnProfile);
        ImageButton btnCart = findViewById(R.id.btnCart);
        etSearch = findViewById(R.id.etSearch);
        recyclerProducts = findViewById(R.id.recyclerProducts);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            tvCustomerName.setText("Hello, " + user.getEmail());
        }

        btnProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
        });

        btnCart.setOnClickListener(v -> {
            Toast.makeText(this, "Cart clicked", Toast.LENGTH_SHORT).show();
        });

        setupRecyclerView();
        loadProductsRealtime(); // Use real-time listener
    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList, this);
        recyclerProducts.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerProducts.setAdapter(adapter);
    }

    private void loadProductsRealtime() {
        db.collection("products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(CustomerDashboardActivity.this, "Error loading products", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshots != null) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    case ADDED:
                                        Product newProduct = dc.getDocument().toObject(Product.class);
                                        newProduct.setId(dc.getDocument().getId());
                                        productList.add(newProduct);
                                        adapter.notifyItemInserted(productList.size() - 1);
                                        break;

                                    case MODIFIED:
                                        Product updatedProduct = dc.getDocument().toObject(Product.class);
                                        updatedProduct.setId(dc.getDocument().getId());
                                        for (int i = 0; i < productList.size(); i++) {
                                            if (productList.get(i).getId().equals(updatedProduct.getId())) {
                                                productList.set(i, updatedProduct);
                                                adapter.notifyItemChanged(i);
                                                break;
                                            }
                                        }
                                        break;

                                    case REMOVED:
                                        String removedId = dc.getDocument().getId();
                                        for (int i = 0; i < productList.size(); i++) {
                                            if (productList.get(i).getId().equals(removedId)) {
                                                productList.remove(i);
                                                adapter.notifyItemRemoved(i);
                                                break;
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    }
                });
    }
}
