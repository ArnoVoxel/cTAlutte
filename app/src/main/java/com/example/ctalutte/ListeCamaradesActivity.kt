package com.example.ctalutte

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class ListeCamaradesActivity : AppCompatActivity() {
    // constantes pour la connexion
    val DB_NAME = "lutte";
    val DB_VERSION = 1;

    // constantes pour les sharedPreferences
    val MES_PREFS = "dossier_camarade"
    val KEY_NOM_PREFS = "nom_du_camarade"
    val KEY_SESSION_OUVERTE = "session_active"
    val KEY_NB_TACHES = "nb_taches_finies"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_camarades)

        val boutonRetour = findViewById<Button>(R.id.bouton_liste_camarades_retour)
        boutonRetour.setOnClickListener(View.OnClickListener {
            finish()
        })

        //récupération de la liste des éléments en BDD
        val connexionBDD = GestionBDD(this, DB_NAME, null, DB_VERSION)
        var listeCamarades = connexionBDD.getListeInfosCamarades(true)

        var listeTemp = arrayListOf<String>()

        for(Camarade in listeCamarades){
            Outils.logPerso("ListeCamaradeItem", Camarade.nomCamarade)
            listeTemp.add(Camarade.nomCamarade + ", score : "+Camarade.scoreCamarade + ", tâches terminées : " + Camarade.nb_taches)
        }
            //adapter pour remplir dynamiquement le listView
        var adapterCamarades = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeTemp)
        Outils.logPerso("ListeCamaradeItem", listeTemp.toString())
        var tableauCamarades = findViewById<ListView>(R.id.tableau_camarades)
        tableauCamarades.adapter = adapterCamarades

        tableauCamarades.setOnItemClickListener{parent, view, position, id ->
            val element = adapterCamarades.getItem(position)
            val intent = Intent(this, BureauActivity::class.java)
            Outils.logPerso("repriseTravail", element.toString())
            intent.putExtra("camarade", element)

            //récupération du nom issu de element
            val nom = element.toString().substringBefore(",")
            Outils.logPerso("repriseTravail", nom)

            //ajout des éléments en prefs
            val prefs = this?.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
            val prefsEditor = prefs.edit()
            prefsEditor.putBoolean(KEY_SESSION_OUVERTE, true)
            prefsEditor.putString(KEY_NOM_PREFS, nom)
            prefsEditor.commit()

            startActivity(intent)
        }

    }
}


