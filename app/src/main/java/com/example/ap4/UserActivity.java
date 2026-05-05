package com.example.ap4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ap4.model.Adherent;
import com.example.ap4.model.LogReponse;
import com.example.ap4.network.ApiService;
import com.example.ap4.network.RetrofitClient;
import com.example.ap4.session.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvPhone;
    private Button btnEdit, btnDelete, btnLogout;
    private ApiService apiService;
    private Adherent currentAdherent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiService = RetrofitClient.getInstance(this).create(ApiService.class);

        initViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyInfo();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvPhone = findViewById(R.id.tvProfilePhone);
        btnEdit = findViewById(R.id.btnEditProfile);
        btnDelete = findViewById(R.id.btnDeleteAccount);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setupListeners() {
        btnEdit.setOnClickListener(v -> {
            if (currentAdherent != null) {
                Intent intent = new Intent(this, CreateAdminActivity.class);
                intent.putExtra("adherent", currentAdherent);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(v -> confirmDelete());

        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void logout() {
        TokenManager.clear(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loadMyInfo() {
        int myId = TokenManager.getUserId(this);
        if (myId == -1) return;

        apiService.getAdherents().enqueue(new Callback<List<Adherent>>() {
            @Override
            public void onResponse(Call<List<Adherent>> call, Response<List<Adherent>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Adherent a : response.body()) {
                        if (a.getId() == myId) {
                            currentAdherent = a;
                            displayInfo(a);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Adherent>> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayInfo(Adherent a) {
        tvName.setText(a.getNom() + " " + a.getPrenom());
        tvEmail.setText(a.getEmail());
        tvPhone.setText(a.getTelephone());
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer mon compte")
                .setMessage("Êtes-vous sûr de vouloir supprimer votre compte ? Cette action est irréversible.")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    apiService.deleteAdherent(currentAdherent.getId()).enqueue(new Callback<LogReponse>() {
                        @Override
                        public void onResponse(Call<LogReponse> call, Response<LogReponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(UserActivity.this, "Compte supprimé", Toast.LENGTH_SHORT).show();
                                logout();
                            }
                        }
                        @Override
                        public void onFailure(Call<LogReponse> call, Throwable t) {}
                    });
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
