package com.example.ctalutte;

public class CamaradeInfos {
    String nom_joueur;
    Integer score;
    String tache_finie;
    Integer nb_taches;

    public CamaradeInfos(){}

    public  CamaradeInfos(String nom_joueur, Integer score, String tache_finie, Integer nb_taches){
        this.nom_joueur = nom_joueur;
        this.score = score;
        this.tache_finie = tache_finie;
        this.nb_taches = nb_taches;
    }

    public String getNomCamarade(){
        return this.nom_joueur;
    }

    public Integer getScoreCamarade(){
        return this.score;
    }

    public String getTache_finie(){
        return this.tache_finie;
    }

    public Integer getNb_taches(){
        return this.nb_taches;
    }

    public void setNom_joueur(String nom_joueur){
        this.nom_joueur = nom_joueur;
    }

    public void setScore(Integer score){
        this.score = score;
    }

    public void setTache_finie(String tache_finie){
        this.tache_finie = tache_finie;
    }

    public void setNb_taches(Integer nb_taches){
        this.nb_taches = nb_taches;
    }
}