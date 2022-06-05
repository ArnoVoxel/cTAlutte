package com.example.ctalutte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class ListeCamaradesActivity : AppCompatActivity() {
    // constantes pour la connexion
    val DB_NAME = "lutte";
    val DB_VERSION = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_camarades)

        val boutonRetour = findViewById<Button>(R.id.bouton_liste_camarades_retour)
        boutonRetour.setOnClickListener(View.OnClickListener {
            finish()
        })

        Outils.logPerso("ListeCamarades", "entrée liste activity")
        //récupération de la liste des éléments en BDD
        val connexionBDD = GestionBDD(this, DB_NAME, null, DB_VERSION)
        Outils.logPerso("ListeCamarades", "connexion BDD")
        var listeCamarades = connexionBDD.getListeInfosCamarades()
        Outils.logPerso("ListeCamarades", "après récupération de la liste")

        var listeTemp = arrayListOf<String>()

        for(Camarade in listeCamarades){
            var infoCamarade = "nom : " + Camarade.nomCamarade +", score : " + Camarade.scoreCamarade + ", taches terminées : " + Camarade.nb_taches
            Outils.logPerso("Camarades",infoCamarade)

//            var listeCamarades = findViewById<TextView>(R.id.liste_camarades)
//            listeCamarades.append("\nnom : ")
//            listeCamarades.append(Camarade.nomCamarade)
//            listeCamarades.append(", score : ")
//            listeCamarades.append(Camarade.scoreCamarade.toString())
//            listeCamarades.append(", tâches terminées : ")
//            listeCamarades.append(Camarade.nb_taches.toString())

            Outils.logPerso("ListeCamaradeItem", Camarade.nomCamarade)
            listeTemp.add(Camarade.nomCamarade + ", score : "+Camarade.scoreCamarade + ", tâches terminées : " + Camarade.nb_taches)
        }
            //adapter pour remplir dynamiquement le listView
        var adapterCamarades = ArrayAdapter<String>(this, com.google.android.material.R.layout.abc_action_menu_item_layout, listeTemp)
        Outils.logPerso("ListeCamaradeItem", listeTemp.toString())
        var tableauCamarades = findViewById<ListView>(R.id.tableau_camarades)
        tableauCamarades.adapter = adapterCamarades
    }
}