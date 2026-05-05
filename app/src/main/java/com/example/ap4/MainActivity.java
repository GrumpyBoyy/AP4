package com.example.ap4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ap4.model.LogRequest;
import com.example.ap4.model.LogReponse;
import com.example.ap4.network.ApiService;
import com.example.ap4.network.RetrofitClient;
import com.example.ap4.session.TokenManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupListeners();
    }

    private void initViews() {
        txtEmail = findViewById(R.id.etEmail); // Corrigé pour correspondre au nouveau layout
        txtPassword = findViewById(R.id.etPassword); // Corrigé
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = txtEmail.getText().toString().trim();
        String pass = txtPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        LogRequest authRequest = new LogRequest(email, pass);
        ApiService api = RetrofitClient.getInstance(this).create(ApiService.class);

        api.login(authRequest).enqueue(new Callback<LogReponse>() {
            @Override
            public void onResponse(Call<LogReponse> call, Response<LogReponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    LogReponse res = response.body();

                    // Sauvegarde du token ET des infos utilisateur
                    TokenManager.saveToken(MainActivity.this, res.getToken());
                    if (res.getData() != null && res.getData().getUser() != null) {
                        TokenManager.saveUser(MainActivity.this, 
                                res.getData().getUser().getId(), 
                                res.getData().getUser().getRole());
                    }

                    redirectByUserRole(res.getRole());
                    return;
                }

                handleApiError(response);
            }

            @Override
            public void onFailure(Call<LogReponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void handleApiError(Response<LogReponse> response) {
        try {
            String error = response.errorBody() != null ? response.errorBody().string() : "Erreur inconnue";
            Toast.makeText(this, "Erreur " + response.code(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectByUserRole(int role) {
        Intent intent;
        if (role == 1) {
            intent = new Intent(MainActivity.this, AdminActivity.class);
        } else {
            intent = new Intent(MainActivity.this, UserActivity.class);
        }
        startActivity(intent);
        finish();
    }
}