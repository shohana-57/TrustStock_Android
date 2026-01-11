package com.example.truststock.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.truststock.R;
import com.example.truststock.model.Product;
import com.example.truststock.manager.CartManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;
    private List<Product> originalList;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.originalList = new ArrayList<>();
        this.context = context;
    }

    public void setOriginalList(List<Product> list) {
        originalList.clear();
        originalList.addAll(list);
    }


    public void filter(String text) {
        productList.clear();
        if (text.isEmpty()) {
            productList.addAll(originalList);
        } else {
            text = text.toLowerCase();
            for (Product p : originalList) {
                if (p.getName().toLowerCase().contains(text)) {
                    productList.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }
 @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("$" + product.getPrice());

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.imgProduct);

        long stock = product.getStock();

     Float avgRating = ratingMap.get(product.getName());

     if (avgRating != null) {
         holder.rbAverageRating.setRating(avgRating);
         holder.rbAverageRating.setVisibility(View.VISIBLE);
     } else {
         holder.rbAverageRating.setRating(0f);
         holder.rbAverageRating.setVisibility(View.GONE);
     }

        if (stock <= 0) {
            holder.btnAdd.setEnabled(false);
            holder.btnAdd.setText("Out of Stock");
            holder.btnAdd.setBackgroundColor(
                    ContextCompat.getColor(context, android.R.color.darker_gray)
            );
        } else {
            holder.btnAdd.setEnabled(true);
            holder.btnAdd.setText("Add to Cart");
        }


        holder.btnAdd.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enter Quantity");

            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint("Quantity");
            builder.setView(input);

            builder.setPositiveButton("Add", (dialog, which) -> {

                String value = input.getText().toString().trim();

                if (value.isEmpty()) {
                    Toast.makeText(context, "Enter quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                int qty = Integer.parseInt(value);

                if (qty <= 0) {
                    Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (qty > product.getStock()) {
                    Toast.makeText(context, "Not enough stock", Toast.LENGTH_SHORT).show();
                    return;
                }

                CartManager.addToCart(product, qty);
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    @Override
    public int getItemCount() { return productList.size(); }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice;
        Button btnAdd;
        RatingBar rbAverageRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            btnAdd = itemView.findViewById(R.id.btnAddToCart);
            rbAverageRating = itemView.findViewById(R.id.rbAverageRating);
        }

    }

    private Map<String, Float> ratingMap = new HashMap<>();

    public void setRatingMap(Map<String, Float> map) {
        this.ratingMap = map;
        notifyDataSetChanged();
    }

}


