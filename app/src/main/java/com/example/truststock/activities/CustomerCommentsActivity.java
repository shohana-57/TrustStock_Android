package com.example.truststock.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truststock.R;
import com.example.truststock.model.Comment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomerCommentsActivity extends AppCompatActivity {

    private EditText etComment, etProductName;
    private RatingBar ratingBar;
    private Button btnSubmit;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_comments);

        etComment = findViewById(R.id.etComment);
        etProductName = findViewById(R.id.etProductName);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmit = findViewById(R.id.btnSubmitComment);

        db = FirebaseFirestore.getInstance();

        btnSubmit.setOnClickListener(v -> submitComment());
    }

    private void submitComment() {
        String commentText = etComment.getText().toString().trim();
        String productName = etProductName.getText().toString().trim();
        int rating = (int) ratingBar.getRating();

        if (commentText.isEmpty() || productName.isEmpty() || rating == 0) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

//        Comment comment = new Comment();
//        comment.setCustomerName("Customer");
//        comment.setProductName(productName);
//        comment.setComment(commentText);
//        comment.setRating(rating);
//
//        db.collection("comments")
//                .add(comment)
//                .addOnSuccessListener(doc -> {
//                    Toast.makeText(this, "Comment submitted", Toast.LENGTH_SHORT).show();
//                    finish();
//                })
//                .addOnFailureListener(e ->
//                        Toast.makeText(this, "Failed to submit", Toast.LENGTH_SHORT).show());
//    }
        Comment comment = new Comment();
        comment.setCustomerName("Customer");
        comment.setProductName(productName);
        comment.setComment(commentText);
        comment.setRating(rating);


        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("customerName", comment.getCustomerName());
        commentMap.put("productName", comment.getProductName());
        commentMap.put("comment", comment.getComment());
        commentMap.put("rating", comment.getRating());
        commentMap.put("timestamp", System.currentTimeMillis());

        db.collection("comments")
                .add(commentMap)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Comment submitted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to submit", Toast.LENGTH_SHORT).show());
    }
}


