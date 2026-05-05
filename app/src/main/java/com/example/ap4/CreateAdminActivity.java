package com.example.ap4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ap4.model.Adherent;
import com.example.ap4.model.LogReponse;
import com.example.ap4.network.ApiService;
import com.example.ap4.network.RetrofitClient;
import com.example.ap4.session.TokenManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAdminActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etEmail, etTel, etPassword;
    private TextView tvTitle, tvRoleLabel;
    private Spinner spinnerRole;
    private Button btnSave;
    private ApiService apiService;
    private Adherent adherentToEdit;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin);

        apiService = RetrofitClient.getInstance(this).create(ApiService.class);

        tvTitle = findViewById(R.id.tvTitle);
        tvRoleLabel = findViewById(R.id.tvRoleLabel);
        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etEmail = findViewById(R.id.etEmail);
        etTel = findViewById(R.id.etTel);
        etPassword = findViewById(R.id.etPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnSave = findViewById(R.id.btnSave);

        String[] roles = {"Client (0)", "Administrateur (1)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        // On n'affiche le choix du rôle QUE en mode édition
        if (isEditMode && TokenManager.getUserRole(this) == 1) {
            tvRoleLabel.setVisibility(View.VISIBLE);
            spinnerRole.setVisibility(View.VISIBLE);
        } else {
            tvRoleLabel.setVisibility(View.GONE);
            spinnerRole.setVisibility(View.GONE);
        }

        if (getIntent().hasExtra("adherent")) {
            adherentToEdit = (Adherent) getIntent().getSerializableExtra("adherent");
            isEditMode = true;
            setupEditMode();
        }

        btnSave.setOnClickListener(v -> saveUser());
    }

    private void setupEditMode() {
        tvTitle.setText("Modifier l'utilisateur");
        btnSave.setText("Enregistrer les modifications");
        etNom.setText(adherentToEdit.getNom());
        etPrenom.setText(adherentToEdit.getPrenom());
        etEmail.setText(adherentToEdit.getEmail());
        etTel.setText(adherentToEdit.getTelephone());
        
        // En mode édition pour un admin, on montre le sélecteur de rôle
        if (TokenManager.getUserRole(this) == 1) {
            tvRoleLabel.setVisibility(View.VISIBLE);
            spinnerRole.setVisibility(View.VISIBLE);
            if (adherentToEdit.getRole() == 1) {
                spinnerRole.setSelection(1);
            } else {
                spinnerRole.setSelection(0);
            }
        }
        
        etPassword.setHint("Nouveau mot de passe (laisser vide pour garder l'ancien)");
    }

    private void saveUser() {
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String tel = etTel.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        int role;
        if (isEditMode && TokenManager.getUserRole(this) == 1) {
            // En modification par un admin : utilise le spinner
            role = spinnerRole.getSelectedItemPosition();
        } else if (isEditMode) {
            // En modification par le client lui-même : garde son rôle
            role = adherentToEdit.getRole();
        } else {
            // En création (Inscription) : toujours Client par défaut
            role = 0;
        }

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || tel.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            Map<String, String> fields = new HashMap<>();
            fields.put("IdAdherents", String.valueOf(adherentToEdit.getId()));
            fields.put("Nom", nom);
            fields.put("Prenom", prenom);
            fields.put("AdresseMail", email);
            fields.put("NumeroTel", tel);
            fields.put("idAbonnement", String.valueOf(adherentToEdit.getIdAbonnement()));
            fields.put("Role", String.valueOf(role));
            
            if (!password.isEmpty()) {
                fields.put("Motdepasse", password);
            }

            apiService.updateAdherent(adherentToEdit.getId(), fields).enqueue(new Callback<LogReponse>() {
                @Override
                public void onResponse(Call<LogReponse> call, Response<LogReponse> response) {
                    if (response.isSuccessful() || response.code() == 302 || response.code() == 303) {
                        Toast.makeText(CreateAdminActivity.this, "Modifié avec succès !", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CreateAdminActivity.this, "Erreur " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<LogReponse> call, Throwable t) {
                    Toast.makeText(CreateAdminActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            if (password.isEmpty()) {
                Toast.makeText(this, "Mot de passe obligatoire", Toast.LENGTH_SHORT).show();
                return;
            }
            
            apiService.createAdherent(nom, prenom, email, tel, password, 1, role).enqueue(new Callback<LogReponse>() {
                @Override
                public void onResponse(Call<LogReponse> call, Response<LogReponse> response) {
                    if (response.isSuccessful() || response.code() == 302 || response.code() == 303) {
                        Toast.makeText(CreateAdminActivity.this, "Créé avec succès !", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CreateAdminActivity.this, "Erreur création", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<LogReponse> call, Throwable t) {
                    Toast.makeText(CreateAdminActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
