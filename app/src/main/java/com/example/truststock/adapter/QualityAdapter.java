package com.example.truststock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truststock.R;
import com.example.truststock.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class QualityAdapter extends RecyclerView.Adapter<QualityAdapter.QualityViewHolder> {

    private List<Product> products;
    private Context context;
    private FirebaseFirestore db;

    public QualityAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public QualityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_product_quality, parent, false);
        return new QualityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QualityViewHolder holder, int position) {
        Product product = products.get(position);

        holder.tvName.setText(product.getName());
        holder.tvQuality.setText("Quality: " + product.getStatus());

        holder.btnGood.setOnClickListener(v ->
                updateQuality(product, "active", position)
        );

        holder.btnAverage.setOnClickListener(v ->
                updateQuality(product, "average", position)
        );

        holder.btnBad.setOnClickListener(v ->
                updateQuality(product, "bad", position)
        );
    }

    private void updateQuality(Product product, String quality, int position) {
        db.collection("products")
                .document(product.getId())
                .update("status", quality)
                .addOnSuccessListener(aVoid -> {
                    product.setStatus(quality);
                    notifyItemChanged(position);
                    Toast.makeText(context,
                            "Quality updated to " + quality,
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class QualityViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvQuality;
        Button btnGood, btnAverage, btnBad;

        public QualityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvQuality = itemView.findViewById(R.id.tvQuality);
            btnGood = itemView.findViewById(R.id.btnGood);
            btnAverage = itemView.findViewById(R.id.btnAverage);
            btnBad = itemView.findViewById(R.id.btnBad);
        }
    }
}

