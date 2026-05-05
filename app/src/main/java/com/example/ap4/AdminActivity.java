package com.example.ap4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ap4.adapter.AdminAdapter;
import com.example.ap4.model.Adherent;
import com.example.ap4.model.LogReponse;
import com.example.ap4.network.ApiService;
import com.example.ap4.network.RetrofitClient;
import com.example.ap4.session.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity implements AdminAdapter.OnAdminClickListener {

    private Button btnGoToCreate, btnLogout;
    private RecyclerView rvAdmins;
    private AdminAdapter adapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiService = RetrofitClient.getInstance(this).create(ApiService.class);

        btnGoToCreate = findViewById(R.id.button_go_to_create);
        btnLogout = findViewById(R.id.btnLogoutAdmin);
        rvAdmins = findViewById(R.id.rvAdmins);

        rvAdmins.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminAdapter(new ArrayList<>(), this);
        rvAdmins.setAdapter(adapter);

        btnGoToCreate.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, CreateAdminActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            TokenManager.clear(this);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdherents();
    }

    private void loadAdherents() {
        apiService.getAdherents().enqueue(new Callback<List<Adherent>>() {
            @Override
            public void onResponse(Call<List<Adherent>> call, Response<List<Adherent>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Adherent>> call, Throwable t) {
                Toast.makeText(AdminActivity.this, "Erreur chargement", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(Adherent admin) {
        Intent intent = new Intent(this, CreateAdminActivity.class);
        intent.putExtra("adherent", admin);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Adherent admin) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer")
                .setMessage("Voulez-vous supprimer " + admin.getNom() + " ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    apiService.deleteAdherent(admin.getId()).enqueue(new Callback<LogReponse>() {
                        @Override
                        public void onResponse(Call<LogReponse> call, Response<LogReponse> response) {
                            if (response.isSuccessful()) loadAdherents();
                        }

                        @Override
                        public void onFailure(Call<LogReponse> call, Throwable t) {}
                    });
                })
                .setNegativeButton("Non", null)
                .show();
    }
}
