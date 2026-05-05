package com.example.ap4.network;

import com.example.ap4.model.Adherent;
import com.example.ap4.model.LogReponse;
import com.example.ap4.model.LogRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("Auth/ConnexionInscription")
    Call<LogReponse> login(@Body LogRequest authRequest);

    @GET("api/Adherents")
    Call<List<Adherent>> getAdherents();

    @FormUrlEncoded
    @POST("Inscription")
    Call<LogReponse> createAdherent(
            @Field("Nom") String nom,
            @Field("Prenom") String prenom,
            @Field("AdresseMail") String email,
            @Field("NumeroTel") String tel,
            @Field("Motdepasse") String mdp,
            @Field("idAbonnement") int idAbos,
            @Field("Role") int role
    );

    @FormUrlEncoded
    @POST("api/Adherents/update/{id}")
    Call<LogReponse> updateAdherent(
            @Path("id") int id,
            @FieldMap Map<String, String> fields
    );

    @DELETE("api/Adherents/{id}")
    Call<LogReponse> deleteAdherent(@Path("id") int id);
}
