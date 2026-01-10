package com.example.truststock.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truststock.R;
import com.example.truststock.adapter.CommentAdapter;
import com.example.truststock.model.Comment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CommentsManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerComments;
    private CommentAdapter adapter;
    private List<Comment> commentList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_management);

        recyclerComments = findViewById(R.id.recyclerComments);
        commentList = new ArrayList<>();
        adapter = new CommentAdapter(commentList);
        recyclerComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerComments.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadComments();
    }

    private void loadComments() {
        db.collection("comments")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshot -> {
                    commentList.clear();

                    for (var doc : snapshot.getDocuments()) {
                        Comment c = doc.toObject(Comment.class);
                        if (c != null) {
                            c.setId(doc.getId());
                            commentList.add(c);
                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );



    }

}

