package com.example.ap4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ap4.R;
import com.example.ap4.model.Adherent;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {

    private List<Adherent> adminList;
    private OnAdminClickListener listener;

    public interface OnAdminClickListener {
        void onEditClick(Adherent admin);
        void onDeleteClick(Adherent admin);
    }

    public AdminAdapter(List<Adherent> adminList, OnAdminClickListener listener) {
        this.adminList = adminList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        Adherent admin = adminList.get(position);
        holder.tvName.setText(admin.getNom() + " " + admin.getPrenom());
        holder.tvEmail.setText(admin.getEmail());
        
        String roleText = (admin.getRole() == 1) ? "Administrateur" : "Client";
        holder.tvRole.setText("Rôle : " + roleText);

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(admin));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(admin));
    }

    @Override
    public int getItemCount() {
        return adminList.size();
    }

    public void updateList(List<Adherent> newList) {
        this.adminList = newList;
        notifyDataSetChanged();
    }

    static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvRole;
        ImageButton btnEdit, btnDelete;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvEmail = itemView.findViewById(R.id.tvItemEmail);
            tvRole = itemView.findViewById(R.id.tvItemRole);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
