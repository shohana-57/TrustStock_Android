package com.example.truststock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.truststock.R;
import com.google.firebase.auth.FirebaseAuth;

public class StaffDashboardActivity extends AppCompatActivity {

    private Button btnManageOrders, btnLowStock, btnProductQuality, btnCustomerComments, btnLogout;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        auth = FirebaseAuth.getInstance();

        btnManageOrders = findViewById(R.id.btnManageOrders);
        btnLowStock = findViewById(R.id.btnLowStock);
        btnProductQuality = findViewById(R.id.btnProductQuality);
        btnCustomerComments = findViewById(R.id.btnCustomerComments);
        btnLogout = findViewById(R.id.btnLogout);


        btnManageOrders.setOnClickListener(v -> {
            startActivity(new Intent(this, StaffOrdersActivity.class));
        });


        btnLowStock.setOnClickListener(v -> {
            startActivity(new Intent(this, LowStockActivity.class));
        });


        btnProductQuality.setOnClickListener(v -> {
            startActivity(new Intent(this, QualityManagementActivity.class));
        });


        btnCustomerComments.setOnClickListener(v -> {
            startActivity(new Intent(this, CommentsManagementActivity.class));
        });

        btnLogout.setOnClickListener(v -> { auth.signOut(); finish(); }); }

    }
