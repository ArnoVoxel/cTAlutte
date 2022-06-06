package com.example.ctalutte

import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.widget.*
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
    // constantes pour la connexion
    val DB_NAME = "lutte";
    val DB_VERSION = 1;

    // constantes pour les sharedPreferences
    val MES_PREFS = "dossier_camarade"
    val KEY_NOM_PREFS = "nom_du_camarade"
    val KEY_SESSION_OUVERTE = "session_active"
    val KEY_NB_TACHES = "nb_taches_finies"

    private lateinit var startMenuLayout: View



    override fun onCreate(savedInstanceState: Bundle?) {
        //Affichage en plein écran + suprresion barre status
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        }
//        else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
//        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // test si session active en préférences
        val prefs = this?.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
        val prefsEditor = prefs.edit()

        var sessionActive = false

        val nomMurCamarade = prefs.getString(KEY_NOM_PREFS, "CAMARADE")
        /*if(nomMurCamarade.equals("CAMARADE")){
            sessionActive = prefs.getBoolean(KEY_SESSION_OUVERTE, false)
            Outils.logPerso("lancement", "if false : "+sessionActive.toString())
        } else {
            prefsEditor.putBoolean(KEY_SESSION_OUVERTE, true)
            Outils.logPerso("lancement", "if true : "+sessionActive.toString())
            startMenuLayout.visibility = View.GONE
            abonnerBoutons()
        }*/

        startMenuLayout = findViewById(R.id.modal_accueil)

        val boutonAdhesion = findViewById<Button>(R.id.bouton_valider_modal)
        boutonAdhesion.setOnClickListener(View.OnClickListener {
            val prefs = this?.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
            sessionActive = prefs.getBoolean(KEY_SESSION_OUVERTE, false)

            //contrôle du champ sur le nom du camarade
            var nomCamarade = findViewById<EditText>(R.id.champ_nom_utilisateur)
            if(nomCamarade.length()<1){
                Outils.toastLong(this, "Veuillez entrer un nom")
            } else {
                validerAdhesion()
                startMenuLayout.visibility = View.GONE
                abonnerBoutons()
            }
        })

        //affichage du bouton reprendre uniquement si nom existant en BDD
        val connexionBDD = GestionBDD(this, DB_NAME, null, DB_VERSION)
        var listeCamarades = connexionBDD.getListeInfosCamarades()
        val boutonContinuer = findViewById<Button>(R.id.bouton_continuer_modal)

        Outils.logPerso("connexion", listeCamarades.size.toString())

        if(listeCamarades.size.toInt().equals(0)){
            boutonContinuer.visibility = View.GONE
        } else {

        boutonContinuer.setOnClickListener(View.OnClickListener {
            Outils.logPerso("MainActivity", "entrée setOnClick Continuer")
            val intentListe = Intent(this, ListeCamaradesActivity::class.java)
            Outils.logPerso("MainActivity", "création de intent")
            startActivity(intentListe)
            Outils.logPerso("MainActivity", "lancement liste camarades")
        })
        }

    }

    /**
     * ajouter un Camarade en base de donnée et enregistre en session à partir du nom inscrit dans le champ
     */
    fun validerAdhesion(){
        Outils.logPerso("connexion", "entrée fct validerAdhesion")

        var nomCamarade = findViewById<EditText>(R.id.champ_nom_utilisateur)


        val connexionBDD = GestionBDD(this, DB_NAME, null, DB_VERSION)

        connexionBDD.ajouterCamarade(nomCamarade.text.toString(), 0, 0, "oui")

        // ajout du nom en préférences
        val prefs = this?.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
        val prefsEditor = prefs.edit()
        prefsEditor.putString(KEY_NOM_PREFS,nomCamarade.text.toString())
        prefsEditor.putBoolean(KEY_SESSION_OUVERTE, true)
        prefsEditor.commit()

        // ajout du nom dans le TextView
        val nomMurCamarade = prefs.getString(KEY_NOM_PREFS, "CAMARADE")
        val vueNomCamarade = findViewById<TextView>(R.id.identite_camarade)
        vueNomCamarade.text = nomMurCamarade

        // ajout du nombre de tâches réalisées
        prefsEditor.putInt(KEY_NB_TACHES,0)
        prefsEditor.commit()

        // ajout du nb de tâches finies dans le TextView
        val nbTachesMur = prefs.getInt(KEY_NB_TACHES, 0)
        val vueNbTachesMur = findViewById<TextView>(R.id.nb_taches)
        vueNbTachesMur.text = nbTachesMur.toString()



    }

    /**
     * Récupère les infos en Préférences pour l'affichage
     */
    fun afficherInfosCamarade(){
        val prefs = this?.getSharedPreferences(MES_PREFS, MODE_PRIVATE)

        // ajout du nom dans le TextView
        val nomMurCamarade = prefs.getString(KEY_NOM_PREFS, "CAMARADE")
        val vueNomCamarade = findViewById<TextView>(R.id.identite_camarade)
        vueNomCamarade.text = nomMurCamarade
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
            Outils.toastCourt(this, "Tu vas bosser fidèle Camarade")
            val intent = Intent(this, TractActivity::class.java)
            startActivity(intent)
        })
    }

}