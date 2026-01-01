package com.example.truststock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    Button btnStaff, btnCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStaff = findViewById(R.id.btnStaff);
        btnCustomer = findViewById(R.id.btnCustomer);

        btnStaff.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StaffLoginActivity.class);
            startActivity(intent);
        });

        btnCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CustomerDashboardActivity.class);
            startActivity(intent);
        });
    }
}
