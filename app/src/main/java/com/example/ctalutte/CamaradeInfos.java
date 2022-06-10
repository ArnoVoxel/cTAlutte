package com.example.ctalutte;

public class CamaradeInfos {
    String nom_joueur;
    Integer score;
    String tache_finie;
    Integer nb_taches;
    Integer tps_centrale;
    String etat_partie;

    public CamaradeInfos(){}

    public  CamaradeInfos(String nom_joueur, Integer score, String tache_finie, Integer nb_taches, Integer tps_centrale, String etat_partie){
        this.nom_joueur = nom_joueur;
        this.score = score;
        this.tache_finie = tache_finie;
        this.nb_taches = nb_taches;
        this.tps_centrale = tps_centrale;
        this.etat_partie = etat_partie;
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

    public Integer getTps_centrale() {return this.tps_centrale;}

    public String getEtat_partie() {return this.etat_partie;}

    public void setNom_joueur(String nom_joueur){
        this.nom_joueur = nom_joueur;
    }

    public void setScore(Integer score){
        this.score = score;
    }

    public void setTache_finie(String tache_finie){ this.tache_finie = tache_finie;}

    public void setNb_taches(Integer nb_taches){
        this.nb_taches = nb_taches;
    }

    public void setTps_centrale(Integer tps_centrale) {this.tps_centrale = tps_centrale;}

    public void setEtat_partie(String etatPartie) {this.etat_partie = etatPartie;}
}