package com.example.truststock.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truststock.R;
import com.example.truststock.model.CartItem;
import com.example.truststock.model.Order;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class StaffOrdersAdapter extends RecyclerView.Adapter<StaffOrdersAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public StaffOrdersAdapter(List<Order> orderList) { this.orderList = orderList; }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvCustomerName.setText(order.getCustomerName());
        holder.tvAddress.setText(order.getAddress());
        holder.tvPhone.setText(order.getPhone());

        StringBuilder sb = new StringBuilder();
        for (CartItem ci : order.getProducts()) {
            sb.append(ci.getProduct().getName())
                    .append(" (Qty: ").append(ci.getQuantity()).append(")")
                    .append(" ₹").append(ci.getTotalPrice()).append("\n");
        }
        holder.tvProducts.setText(sb.toString());
        holder.tvTotal.setText("Total: $" + order.getTotal());

        holder.btnDelivered.setOnClickListener(v -> {
            db.collection("orders").document(order.getId()).delete()
                    .addOnSuccessListener(aVoid -> {
                        orderList.remove(position);
                        notifyItemRemoved(position);
                    });
        });
    }

    @Override
    public int getItemCount() { return orderList.size(); }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvAddress, tvPhone, tvProducts, tvTotal;
        Button btnDelivered;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvProducts = itemView.findViewById(R.id.tvProducts);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            btnDelivered = itemView.findViewById(R.id.btnDelivered);
        }
    }
}
