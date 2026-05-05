package com.example.ap4.model;

import com.google.gson.annotations.SerializedName;

public class AdherentRequest {
    @SerializedName("IdAdherents")
    private Integer id;
    
    @SerializedName("Nom")
    private String nom;
    
    @SerializedName("Prenom")
    private String prenom;
    
    @SerializedName("AdresseMail")
    private String email;
    
    @SerializedName("NumeroTel")
    private String telephone;
    
    @SerializedName("Motdepasse")
    private String motdepasse;
    
    @SerializedName("idAbonnement")
    private Integer idAbonnement;
    
    @SerializedName("Role")
    private Integer role;

    public AdherentRequest(int id, String nom, String prenom, String email, String telephone, String motdepasse, int idAbonnement, int role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.motdepasse = motdepasse;
        this.idAbonnement = idAbonnement;
        this.role = role;
    }
}
