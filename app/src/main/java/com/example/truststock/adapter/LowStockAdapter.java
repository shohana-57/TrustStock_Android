package com.example.truststock.adapter;

import android.app.AlertDialog;
import android.app.AutomaticZenRule;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truststock.R;
import com.example.truststock.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class LowStockAdapter extends RecyclerView.Adapter<LowStockAdapter.ViewHolder> {

    private List<Product> products;
    private Context context;
    private FirebaseFirestore db;

    public LowStockAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_low_stock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvStock.setText("Stock: " + product.getStock());

        holder.btnUpdateStock.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Update Stock");

            EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint("Enter new stock");
            builder.setView(input);

            builder.setPositiveButton("Update", (dialog, which) -> {
                String value = input.getText().toString().trim();
                if (value.isEmpty()) {
                    Toast.makeText(context, "Stock cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                int newStock = Integer.parseInt(value);

                db.collection("products")
                        .document(product.getId())
                        .update("stock", newStock)
                        .addOnSuccessListener(aVoid -> {
                            product.setStock(newStock);
                            notifyItemChanged(position);
                            if(newStock > 5){
                                products.remove(position);
                                notifyItemRemoved(position);
                            }
                            Toast.makeText(context, "Stock updated", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show());
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvProductName, tvStock;
        Button btnUpdateStock;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvStock = itemView.findViewById(R.id.tvStock);
            btnUpdateStock = itemView.findViewById(R.id.btnUpdateStock);
        }
    }
}



