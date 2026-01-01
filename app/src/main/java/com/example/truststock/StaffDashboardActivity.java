package com.example.truststock;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class StaffDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Welcome Staff Dashboard");
        tv.setTextSize(22);
        tv.setGravity(android.view.Gravity.CENTER);
        setContentView(tv);
    }
}