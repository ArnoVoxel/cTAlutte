package com.example.ctalutte

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
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
    val KEY_TEMPSCENTRALE = "key_temps_centrale"

    //Pour gérer la musique
    var flag = false

    //Pour la centrale
    var decompteCentrale:Long = 0
    lateinit var thermo : ImageView
    var tempsCompteur:Long = 50000
    lateinit var compteur  : CountDownTimer




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bureau)
        thermo = findViewById(R.id.temperature_central)
        onBackPressed()
        abonnerBoutons()
        afficherInfosCamarade()
        compteur = object : CountDownTimer(tempsCompteur, 1000) {
            override fun onTick(millisUntilFinished: Long){
                decompteCentrale = millisUntilFinished / 1000
                fondCentrale(decompteCentrale)
                Outils.logPerso("compteur",decompteCentrale.toString())

            }
            override fun onFinish() {
                Outils.toastCourt(applicationContext,"Fin")

            }
        }
        compteur.start()
    }

    /**
     * Abonnement des boutons pour lancer une tâche ou activer la musique
     */
    fun abonnerBoutons(){
        // abonnement du bouton pour lancer la musique
        var boutonRadio = findViewById<ImageButton>(androidx.appcompat.R.id.radio);
        var chanson = MediaPlayer.create(this,R.raw.international_fr)
        boutonRadio.setOnClickListener(View.OnClickListener {
            if(flag == false){
                chanson.start()
                flag = true;
            } else {
                chanson.stop()
                chanson.prepare()
                flag = false;
            }})

        //abonnement bouton dossier
        var boutonDossier = findViewById<ImageButton>(R.id.bouton_dossier)
        boutonDossier.setOnClickListener(View.OnClickListener {
            //Gestion temps restant centrale :
            val prefs = getSharedPreferences(MES_PREFS, MODE_PRIVATE)
            val prefsEditor = prefs.edit()
            prefsEditor.putLong(KEY_TEMPSCENTRALE,(decompteCentrale * 1000))
            prefsEditor.apply()
            compteur.cancel()


            //Gestion choix activité
            val intentTract = Intent(this, TractActivity::class.java)
            val intentPierre = Intent(this, CassePierreActivity::class.java)

            Outils.toastCourt(this, "Tu vas bosser fidèle Camarade")

            var randomValue = (1..2).random()
            Outils.logPerso("randomActivity", randomValue.toString())

            when(randomValue){
                1 -> startActivity(intentTract)
                2 -> startActivity(intentTract)
            }
        })
        //Abonnement bouton centrale
        var boutonCentrale = findViewById<Button>(R.id.bouton_central)
        boutonCentrale.setOnClickListener(View.OnClickListener { refroidirCentrale() },)


        var boutonRetour = findViewById<ImageButton>(R.id.bouton_retour)
        boutonRetour.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val connexionBDD = GestionBDD(this, DB_NAME, null, DB_VERSION)
            val prefs = getSharedPreferences(MES_PREFS, MODE_PRIVATE)
            val prefsEditor = prefs.edit()
            connexionBDD.setTempsCentrale(prefs.getString(KEY_NOM_PREFS,"CAMARADE"),(decompteCentrale*1000).toInt())
            compteur.cancel()
            startActivity(intent)
        })
    }

    /**
     * Récupère les infos en Préférences pour l'affichage
     */
    fun afficherInfosCamarade(){
        val prefs = this?.getSharedPreferences(MES_PREFS, MODE_PRIVATE)

        //Création connexion
        val connexionBDD = GestionBDD(this, DB_NAME, null, DB_VERSION)

        // ajout du nom dans le TextView
        val prefsEditor = prefs.edit()
        val nomMurCamarade = prefs.getString(KEY_NOM_PREFS, "CAMARADE")
        val vueNomCamarade = findViewById<TextView>(R.id.identite_camarade)
        vueNomCamarade.text = nomMurCamarade

        // ajout du nombre de tâches réalisées

        prefsEditor.putInt(KEY_NB_TACHES,connexionBDD.getNbTaches(nomMurCamarade))
        prefsEditor.apply()


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
        compteur.cancel()
        tempsCompteur = resumeCentrale()
        Outils.logPerso("TestCompteur",tempsCompteur.toString())
        compteur = object : CountDownTimer(tempsCompteur, 1000) {
            override fun onTick(millisUntilFinished: Long){
                decompteCentrale = millisUntilFinished / 1000
                fondCentrale(decompteCentrale)
                Outils.logPerso("compteur",decompteCentrale.toString())

            }
            override fun onFinish() {
                Outils.toastCourt(applicationContext,"Fin")

            }
        }
        compteur.start()

    }

    fun refroidirCentrale(){
        Outils.logPerso("compteur","avant cancel")
        if(decompteCentrale > 10)
        {
            Outils.toastCourt(this,"Trop tôt !")
        } else {
            thermo.setImageDrawable(getDrawable(R.drawable.thermometre_base))
            compteur.cancel()
            tempsCompteur = 50000
            compteur = object : CountDownTimer(tempsCompteur, 1000) {
                override fun onTick(millisUntilFinished: Long){
                    decompteCentrale = millisUntilFinished / 1000
                    fondCentrale(decompteCentrale)
                    Outils.logPerso("compteur",decompteCentrale.toString())

                }
                override fun onFinish() {
                    Outils.toastCourt(applicationContext,"Fin")

                }
            }
            compteur.start()
        }

    }

    fun resumeCentrale():Long{
        val prefs = this.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
//        val tempsRestant = prefs.getLong(KEY_TEMPSCENTRALE,50000)
        val connexionBDD = GestionBDD(this, DB_NAME, null, DB_VERSION)
        val tempsRestant = connexionBDD.getTempsCentrale(prefs.getString(KEY_NOM_PREFS,"CAMARADE")).toLong()

        return tempsRestant
    }

    fun fondCentrale(temps:Long){
        if(temps < 10 && temps >1){
           if(temps>=9){
                Outils.toastCourt(applicationContext, "Il reste : " + decompteCentrale.toString() + " secondes avant BOOM")
           }
            thermo.setImageDrawable(getDrawable(R.drawable.thermometre_etape5))
        } else if(temps <40 && temps > 30){
            thermo.setImageDrawable(getDrawable(R.drawable.thermometre_etape2))
        } else if(temps <=30 && temps > 20) {
            thermo.setImageDrawable(getDrawable(R.drawable.thermometre_etape3))
        }else if(temps <=20 && temps > 10) {
            thermo.setImageDrawable(getDrawable(R.drawable.thermometre_etape4))
        }
    }
}