package com.example.ctalutte

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.function.Predicate


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

        //masquer le titre
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sessionActive = false

        // test si session active en préférences
        val prefs = this?.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
        val prefsEditor = prefs.edit()

        Outils.logPerso("tableauPref", "entrée else bool")
        val connexionBDD = GestionBDD(this, DB_NAME, null, DB_VERSION)

        //liste des noms en BDD
        var listeNomsCamarades = connexionBDD.listeNomsCamarades

        //tableau temporaire pour traitement des noms
        var listeNomsTemp = arrayListOf<String>()

        for(Camarade in listeNomsCamarades){
            listeNomsTemp.add(Camarade.nomCamarade)
        }

        startMenuLayout = findViewById(R.id.modal_accueil)

        val boutonAdhesion = findViewById<Button>(R.id.bouton_valider_modal)
        boutonAdhesion.setOnClickListener(View.OnClickListener {
            val prefs = this?.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
            sessionActive = prefs.getBoolean(KEY_SESSION_OUVERTE, false)

            //contrôle du champ sur le nom du camarade
            var nomCamarade = findViewById<EditText>(R.id.champ_nom_utilisateur)

            var test = nomCamarade.toString() in listeNomsTemp

            if(nomCamarade.length()<1){
                Outils.toastLong(this, "Veuillez entrer un nom")
            } else if(nomCamarade.text.toString() in listeNomsTemp){
                Outils.toastLong(this, "ton Camarade est déjà là !")
            }else {
                validerAdhesion()
                val intentBureau = Intent(this, BureauActivity::class.java)
                startActivity(intentBureau)
            }
        })

        //affichage du bouton reprendre uniquement si nom existant en BDD
        var listeCamarades = connexionBDD.getListeInfosCamarades()
        val boutonContinuer = findViewById<Button>(R.id.bouton_continuer_modal)

        if(listeCamarades.size.toInt().equals(0)){
            boutonContinuer.visibility = View.GONE
        } else {
            boutonContinuer.setOnClickListener(View.OnClickListener {
            val intentListe = Intent(this, ListeCamaradesActivity::class.java)
            startActivity(intentListe)
        })
        }

    }

    /**
     * ajouter un Camarade en base de donnée et enregistre en session à partir du nom inscrit dans le champ
     */
    fun validerAdhesion(){
        var nomCamarade = findViewById<EditText>(R.id.champ_nom_utilisateur)

        val connexionBDD = GestionBDD(this, DB_NAME, null, DB_VERSION)
        connexionBDD.ajouterCamarade(nomCamarade.text.toString(), 0, 0, "oui")

        ajouterCamaradePref(nomCamarade.text.toString())
    }

    fun ajouterCamaradePref(nomCamarade: String){
        // ajout du nom en préférences
        val prefs = this?.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
        val prefsEditor = prefs.edit()
        prefsEditor.putString(KEY_NOM_PREFS,nomCamarade)
        prefsEditor.putBoolean(KEY_SESSION_OUVERTE, true)
        prefsEditor.commit()
    }

}