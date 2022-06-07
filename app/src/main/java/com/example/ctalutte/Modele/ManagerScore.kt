package com.example.ctalutte.Modele

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.ctalutte.GestionBDD
import com.example.ctalutte.Outils

class ManagerScore constructor(var context: Context) {

    // constantes pour la connexion
    val DB_NAME = "lutte";
    val DB_VERSION = 1;

    // constantes pour les sharedPreferences
    val MES_PREFS = "dossier_camarade"
    val KEY_NOM_PREFS = "nom_du_camarade"
    val KEY_SESSION_OUVERTE = "session_active"
    val KEY_NB_TACHES = "nb_taches_finies"
    val KEY_FLAG = "key_flag"

    // activité en cours
    fun startTask(){
        Outils.logPerso("taskManager", "entrée startTask")
        val connexionBDD = GestionBDD(context, DB_NAME, null, DB_VERSION)
        val prefs = context.getSharedPreferences(MES_PREFS, AppCompatActivity.MODE_PRIVATE)
        connexionBDD.commencerTache(prefs.getString(KEY_NOM_PREFS, "CAMARADE"))
    }

    fun stopTask(score:Int){
        Outils.logPerso("taskManager", "entrée stopTask")
        val connexionBDD = GestionBDD(context, DB_NAME, null, DB_VERSION)
        val prefs = context.getSharedPreferences(MES_PREFS, AppCompatActivity.MODE_PRIVATE)
        connexionBDD.terminerTache(prefs.getString(KEY_NOM_PREFS,"CAMARADE"),score )
    }

}