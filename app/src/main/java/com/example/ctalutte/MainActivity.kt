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
import android.content.SharedPreferences
import android.provider.BaseColumns
import android.widget.*
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    // constantes pour la connexion
    val DB_NAME = "lutte";
    val DB_VERSION = 1;

    // constantes pour les sharedPreferences
    val MES_PREFS = "dossier_camarade"
    val KEY_NOM_PREFS = "nom_du_camarade"
    val KEY_NB_TACHES = "nb_taches_finies"

    private lateinit var startMenuLayout: View

    fun validerAdhesion(){

        var nomCamarade = findViewById<EditText>(R.id.champ_nom_utilisateur)
        Outils.logPerso("BDD", "après récupération du nom"+nomCamarade.text.toString())

        val connexionBDD = Connexion(this, DB_NAME,null,DB_VERSION)
        Outils.logPerso("BDD", "après création connexion")
        connexionBDD.ajouterCamarade(nomCamarade.text.toString(), 0, 0, "finie")
        Outils.logPerso("BDD", "après insert")

        //ajout du nom en préférences
        val prefs = this?.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
        val prefsEditor = prefs.edit()
        prefsEditor.putString(KEY_NOM_PREFS,nomCamarade.text.toString())
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

        // cache le menu de connexion
        startMenuLayout = findViewById(R.id.modal_accueil)
        startMenuLayout.visibility = View.GONE

        // abonnement du bouton pour lancer la musique
        var boutonRadio = findViewById<ImageButton>(androidx.appcompat.R.id.radio);
        boutonRadio.setOnClickListener(View.OnClickListener {
            var chanson = MediaPlayer.create(this, R.raw.the_internationale_english)
            chanson?.start()
        })

            //abonnement bouton dossier
        var boutonDossier = findViewById<ImageButton>(R.id.bouton_dossier)
        boutonDossier.setOnClickListener(View.OnClickListener {
            Outils.toastCourt(this, "Tu vas bosser fidèle Camarade")
            val intent = Intent(this, CassePierreActivity::class.java)
            startActivity(intent)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //Affichage en plein écran + suprresion barre status
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val boutonAdhesion = findViewById<Button>(R.id.bouton_valider_modal)
        boutonAdhesion.setOnClickListener(View.OnClickListener {
            validerAdhesion()
        })

    }


}