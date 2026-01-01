package com.example.truststock;

import android.content.Intent;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }


            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = authResult.getUser();
                        if(user != null){

                            db.collection("StaffRoles")
                                    .document(user.getUid())
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                          Toast.makeText(this, "Logged in as " + role, Toast.LENGTH_SHORT).show();

                          if ("admin".equals(role)) {
                              startActivity(new Intent(this, StaffDashboardActivity.class));
                          } else if ("manager".equals(role)) {
                              startActivity(new Intent(this, StaffDashboardActivity.class));
                          } else if("quality checker".equals(role)) {
                              startActivity(new Intent(this, StaffDashboardActivity.class));
                          }
                      }
                                       else {

                                            auth.signOut();
                                            Toast.makeText(this, "Access denied: not staff", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        auth.signOut();
                                        Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Login failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
