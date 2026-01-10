package com.example.truststock.InitialActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.truststock.R;
import com.example.truststock.activities.CustomerDashboardActivity;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerLogin extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, CustomerDashboardActivity.class));
            finish();
        }

        setContentView(R.layout.activity_customer_login);

        EditText email = findViewById(R.id.etEmail);
        EditText password = findViewById(R.id.etPassword);

        findViewById(R.id.btnLogin).setOnClickListener(v ->
                auth.signInWithEmailAndPassword(
                        email.getText().toString(),
                        password.getText().toString()
                ).addOnSuccessListener(r -> {
                    startActivity(new Intent(this, CustomerDashboardActivity.class));
                    finish();
                }));

        findViewById(R.id.btnRegister).setOnClickListener(v ->
                auth.createUserWithEmailAndPassword(
                        email.getText().toString(),
                        password.getText().toString()
                ).addOnSuccessListener(r -> {
                    startActivity(new Intent(this, CustomerDashboardActivity.class));
                    finish();
                }));
    }
}
