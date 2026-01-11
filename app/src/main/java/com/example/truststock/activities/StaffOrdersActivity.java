package com.example.truststock.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truststock.R;
import com.example.truststock.adapter.StaffOrdersAdapter;
import com.example.truststock.model.Order;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StaffOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private StaffOrdersAdapter adapter;
    private List<Order> orderList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_orders);

        recyclerOrders = findViewById(R.id.recyclerOrders);
        orderList = new ArrayList<>();
        adapter = new StaffOrdersAdapter(orderList);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrders.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadOrders();
    }

    private void loadOrders() {
        db.collection("orders").get().addOnSuccessListener(queryDocumentSnapshots -> {
            orderList.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                Order order = doc.toObject(Order.class);
                order.setId(doc.getId());
                orderList.add(order);
            }
            adapter.notifyDataSetChanged();
        });
    }
}
