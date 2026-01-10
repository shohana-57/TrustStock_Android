package com.example.truststock.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truststock.R;
import com.example.truststock.model.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;

    public CommentAdapter(List<Comment> comments) { this.comments = comments; }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment c = comments.get(position);
        holder.tvCustomer.setText(c.getCustomerName());
        holder.tvComment.setText(c.getComment());
        holder.ratingBar.setRating(c.getRating());
        holder.tvProductName.setText("Product: " + c.getProductName());

    }

    @Override
    public int getItemCount() { return comments.size(); }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomer, tvComment,tvProductName;
        RatingBar ratingBar;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomer = itemView.findViewById(R.id.tvCustomerName);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvComment = itemView.findViewById(R.id.tvComment);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}

