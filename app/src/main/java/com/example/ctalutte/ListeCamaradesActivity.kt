package com.example.ctalutte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

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
        for(Camarade in listeCamarades){
            var infoCamarade = "nom : " + Camarade.nomCamarade +", score : " + Camarade.scoreCamarade + ", taches terminées : " + Camarade.nb_taches
            Outils.logPerso("Camarades",infoCamarade)
        }
    }
}