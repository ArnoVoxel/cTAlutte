package com.example.ctalutte

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class BureauActivity: AppCompatActivity() {

    // constantes pour la connexion
    val DB_NAME = "lutte";
    val DB_VERSION = 1;

    // constantes pour les sharedPreferences
    val MES_PREFS = "dossier_camarade"
    val KEY_NOM_PREFS = "nom_du_camarade"
    val KEY_SESSION_OUVERTE = "session_active"
    val KEY_NB_TACHES = "nb_taches_finies"
    val KEY_FLAG = "key_flag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bureau)

        onBackPressed()


        abonnerBoutons()
        afficherInfosCamarade()

    }

    /**
     * Abonnement des boutons pour lancer une tâche ou activer la musique
     */
    fun abonnerBoutons(){
        // abonnement du bouton pour lancer la musique
        var boutonRadio = findViewById<ImageButton>(androidx.appcompat.R.id.radio);
        boutonRadio.setOnClickListener(View.OnClickListener {
            var chanson = MediaPlayer.create(this, R.raw.international_fr)
            chanson?.start()
        })

        //abonnement bouton dossier
        var boutonDossier = findViewById<ImageButton>(R.id.bouton_dossier)
        boutonDossier.setOnClickListener(View.OnClickListener {

            val intentTract = Intent(this, TractActivity::class.java)
            val intentPierre = Intent(this, CassePierreActivity::class.java)

            Outils.toastCourt(this, "Tu vas bosser fidèle Camarade")

            var randomValue = (1..2).random()
            Outils.logPerso("randomActivity", randomValue.toString())

            when(randomValue){
                1 -> startActivity(intentTract)
                2 -> startActivity(intentPierre)
            }

        })

        var boutonRetour = findViewById<ImageButton>(R.id.bouton_retour)
        boutonRetour.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
    }

    /**
     * Récupère les infos en Préférences pour l'affichage
     */
    fun afficherInfosCamarade(){
        val prefs = this?.getSharedPreferences(MES_PREFS, MODE_PRIVATE)

        // ajout du nom dans le TextView
        val prefsEditor = prefs.edit()
        val nomMurCamarade = prefs.getString(KEY_NOM_PREFS, "CAMARADE")
        val vueNomCamarade = findViewById<TextView>(R.id.identite_camarade)
        vueNomCamarade.text = nomMurCamarade

        // ajout du nombre de tâches réalisées

        /*prefsEditor.putInt(KEY_NB_TACHES,0)
        prefsEditor.commit()*/


        // ajout du nb de tâches finies dans le TextView
        val nbTachesMur = prefs.getInt(KEY_NB_TACHES, 0)
        Outils.logPerso("taches", "bureauActivity")
        Outils.logPerso("taches", nbTachesMur.toString())
        val vueNbTachesMur = findViewById<TextView>(R.id.nb_taches)
        vueNbTachesMur.text = nbTachesMur.toString()
    }

    override fun onBackPressed(){

    }

    override fun onResume() {
        super.onResume()
        afficherInfosCamarade()
    }
}