package com.example.truststock.InitialActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.truststock.R;
import com.example.truststock.activities.LowStockActivity;
import com.example.truststock.activities.ProductManagementActivity;
import com.example.truststock.activities.QualityManagementActivity;
import com.example.truststock.activities.StaffDashboardActivity;
import com.example.truststock.activities.StaffOrdersActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class StaffLoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        findViewById(R.id.btnStaffLogin).setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(StaffLoginActivity.this,
                        "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {

                        FirebaseUser user = authResult.getUser();
                        if (user == null) return;

                        db.collection("StaffRoles")
                                .document(user.getUid())
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {

                                    if (!documentSnapshot.exists()) {
                                        auth.signOut();
                                        Toast.makeText(
                                                StaffLoginActivity.this,
                                                "Access denied: not staff",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        return;
                                    }

                                    String role = documentSnapshot.getString("role");
                                    Toast.makeText(
                                            StaffLoginActivity.this,
                                            "Logged in as " + role,
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    Intent intent = null;

                                    switch (role) {
                                        case "admin":
                                            intent = new Intent(
                                                    StaffLoginActivity.this,
                                                    StaffDashboardActivity.class
                                            );
                                            break;
                                        case "quality checker":
                                            intent = new Intent(
                                                    StaffLoginActivity.this,
                                                    QualityManagementActivity.class
                                            );
                                            break;

                                        case "manager":
                                            intent = new Intent(
                                                    StaffLoginActivity.this,
                                                    ProductManagementActivity.class
                                            );
                                            break;

                                        case "delivery":
                                            intent = new Intent(
                                                    StaffLoginActivity.this,
                                                    StaffOrdersActivity.class
                                            );
                                            break;

                                        case "stockmanager":
                                            intent = new Intent(
                                                    StaffLoginActivity.this,
                                                    LowStockActivity.class
                                            );
                                            break;
                                    }

                                    if (intent != null) {
                                        startActivity(intent);
                                        finish();
                                    }

                                })
                                .addOnFailureListener(e -> {
                                    auth.signOut();
                                    Toast.makeText(
                                            StaffLoginActivity.this,
                                            "Error: " + e.getMessage(),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                });

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(
                                StaffLoginActivity.this,
                                "Login failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    });
        });
    }
}
