package com.example.ap4.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Adherent implements Serializable {
    @SerializedName("IdAdherents")
    private int id;
    
    @SerializedName("Nom")
    private String nom;
    
    @SerializedName("Prenom")
    private String prenom;
    
    @SerializedName("AdresseMail")
    private String email;
    
    @SerializedName("NumeroTel")
    private String telephone;
    
    @SerializedName("Role")
    private int role;
    
    @SerializedName("idAbonnement")
    private int idAbonnement;

    @SerializedName("Motdepasse")
    private String motdepasse;

    public Adherent(int id, String nom, String prenom, String email, String telephone, int role, int idAbonnement, String motdepasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.idAbonnement = idAbonnement;
        this.motdepasse = motdepasse;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public int getRole() { return role; }
    public int getIdAbonnement() { return idAbonnement; }
    public String getMotdepasse() { return motdepasse; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setRole(int role) { this.role = role; }
    public void setIdAbonnement(int idAbonnement) { this.idAbonnement = idAbonnement; }
    public void setMotdepasse(String motdepasse) { this.motdepasse = motdepasse; }
}
