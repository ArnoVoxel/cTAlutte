package com.example.ctalutte

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.ContactsContract
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
    lateinit var boutonCentrale : ImageView

    //Pour les dossier
    lateinit var boutonDossier: ImageView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bureau)
        thermo = findViewById(R.id.fond_centrale)
        onBackPressed()
        abonnerBoutons()
        afficherInfosCamarade()
        nombreDossier()
        compteur = object : CountDownTimer(tempsCompteur, 1000) {
            override fun onTick(millisUntilFinished: Long){
                decompteCentrale = millisUntilFinished / 1000
                fondCentrale(decompteCentrale)
                Outils.logPerso("compteur",decompteCentrale.toString())

            }
            override fun onFinish() {
                Outils.toastCourt(applicationContext,"BOOOOM !")
                changerEtatPartie("Hiver nucléaire")
                val intent = Intent(applicationContext, ExplosionActivity::class.java)
                startActivity(intent)

            }
        }
        compteur.start()
    }

    /**
     * Abonnement des boutons pour lancer une tâche ou activer la musique
     */
    fun abonnerBoutons(){
        // abonnement du bouton pour lancer la musique
        val boutonRadio = findViewById<ImageButton>(androidx.appcompat.R.id.radio);
        val chanson = MediaPlayer.create(this,R.raw.international_fr)
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
        boutonDossier = findViewById<ImageButton>(R.id.bouton_dossier)
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
            val intentRunning = Intent(this, EscapeActivity::class.java)

            Outils.toastCourt(this, "Tu vas bosser fidèle Camarade")

            var randomValue = (1..3).random()
            Outils.logPerso("randomActivity", randomValue.toString())

            when(randomValue){
                1 -> startActivity(intentTract)
                2 -> startActivity(intentPierre)
                3 -> startActivity(intentRunning)
            }
        })
        //Abonnement bouton centrale
        boutonCentrale = findViewById<ImageButton>(R.id.bouton_central)
        boutonCentrale.setOnClickListener { refroidirCentrale() }
//        boutonCentrale.setOnClickListener({ testScore() })



        val boutonRetour = findViewById<ImageButton>(R.id.bouton_retour)
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

    /**
     * Comportement de l'activité quand appuie sur le bouton retour
     */
    override fun onBackPressed(){

    }

    /**
     * Comportement de l'activité lors du retour dessus
     */
    override fun onResume() {
        super.onResume()
        afficherInfosCamarade()
        compteur.cancel()
        tempsCompteur = resumeCentrale()
Outils.logPerso("TestCompteur","Dans onResume : " + tempsCompteur.toString())
        when(testFinPartie(tempsCompteur)){
            0->{
                compteur = object : CountDownTimer(tempsCompteur, 1000) {
                override fun onTick(millisUntilFinished: Long){
                    decompteCentrale = millisUntilFinished / 1000
                    fondCentrale(decompteCentrale)
                    Outils.logPerso("compteur",decompteCentrale.toString())

                }
                override fun onFinish() {
                    Outils.toastCourt(applicationContext,"BOOOOM !")
                    changerEtatPartie("Hiver nucléaire")
                    val intent = Intent(applicationContext, ExplosionActivity::class.java)
                    startActivity(intent)

                }
            }
                compteur.start()
                nombreDossier()
Outils.logPerso("finDePartie","cas 0 : " + recupererEtatPartie())
            }
            1->{
                Outils.toastCourt(this,"Vous avez perdu : GOULAAAAG !")
                changerEtatPartie("Goulag")
                val intent = Intent(this, GoulagActivity::class.java)
                startActivity(intent)
Outils.logPerso("finDePartie","cas 1 : " + recupererEtatPartie())
            }
            2->{
                Outils.toastCourt(this,"Victoire ! Vous êtes Leader Suprème !")
                changerEtatPartie("Leader")
                val intent = Intent(this, LeaderActivity::class.java)
                startActivity(intent)
Outils.logPerso("finDePartie","cas 2 : " + recupererEtatPartie())
            }
            3->{
                Outils.toastCourt(this,"BOOOOM !")
                val intent = Intent(this, ExplosionActivity::class.java)
                startActivity(intent)
                changerEtatPartie("Hiver nucléaire")
Outils.logPerso("finDePartie","cas 3 : " + recupererEtatPartie())
            }
        }
    }

    /**
    * Relance le compteur à zéro de la centrale
    */
    fun refroidirCentrale(){
        Outils.logPerso("compteur","avant cancel")
        if(decompteCentrale > 10)
        {
            Outils.toastCourt(this,"Trop tôt !")
        } else {
            thermo.setImageDrawable(getDrawable(R.drawable.centrale_niveau1))
            compteur.cancel()
            tempsCompteur = 50000
            compteur = object : CountDownTimer(tempsCompteur, 1000) {
                override fun onTick(millisUntilFinished: Long){
                    decompteCentrale = millisUntilFinished / 1000
                    fondCentrale(decompteCentrale)
Outils.logPerso("compteur",decompteCentrale.toString())

                }
                override fun onFinish() {
                    Outils.toastCourt(applicationContext,"BOOOOM !")
                    changerEtatPartie("Hiver nucléaire")
                    val intent = Intent(applicationContext, ExplosionActivity::class.java)
                    startActivity(intent)

                }
            }
            compteur.start()
        }

    }
    /**
    *Récupère le temps du compteur quand retour sur le bureau
    */
    fun resumeCentrale():Long{
        val prefs = this.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
//        val tempsRestant = prefs.getLong(KEY_TEMPSCENTRALE,50000)
        val connexionBDD = GestionBDD(this, DB_NAME, null, DB_VERSION)
        val tempsRestant = connexionBDD.getTempsCentrale(prefs.getString(KEY_NOM_PREFS,"CAMARADE")).toLong()
Outils.logPerso("TestCompteur","Dans resumeCentrale : " + tempsRestant.toString())

        return tempsRestant
    }

    /**
     *     Gère la couleur de la centrale en fonction du compteur
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun fondCentrale(temps:Long){
        if(temps < 10 && temps >1){
           if(temps>=9){
                Outils.toastCourt(applicationContext, "Il reste : " + decompteCentrale.toString() + " secondes avant BOOM")
           }
            thermo.setImageDrawable(getDrawable(R.drawable.centrale_niveau5))
            boutonCentrale.visibility = View.VISIBLE
        } else if(temps <40 && temps > 30){
            thermo.setImageDrawable(getDrawable(R.drawable.centrale_niveau2))
            boutonCentrale.visibility = View.GONE
        } else if(temps <=30 && temps > 20) {
            thermo.setImageDrawable(getDrawable(R.drawable.centrale_niveau3))
            boutonCentrale.visibility = View.GONE
        }else if(temps <=20 && temps > 10) {
            thermo.setImageDrawable(getDrawable(R.drawable.centrale_niveau4))
            boutonCentrale.visibility = View.GONE
        }else{
            boutonCentrale.visibility = View.GONE
        }
    }

    /**
     * Récupère l'état de la partie du joueur en BDD
     */
    fun recupererEtatPartie():String{
//Outils.logPerso("testPartie","Entrée Etat Partie")
        val prefs = this.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
        val connexionBDD = GestionBDD(this,DB_NAME,null,DB_VERSION)
        val etatPartie = connexionBDD.getEtatPartie(prefs.getString(KEY_NOM_PREFS,"CAMARADE"))
//Outils.logPerso("testPartie",etatPartie)
        return etatPartie
    }

    /**
     * Change l'état de la partie du joueur en BDD
     */
    fun changerEtatPartie(etat : String){
        val prefs = this.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
        val connexionBDD = GestionBDD(this,DB_NAME,null,DB_VERSION)
        connexionBDD.setEtatPartie(prefs.getString(KEY_NOM_PREFS,"CAMARADE"),etat)
//Outils.logPerso("testPartie",recupererEtatPartie())
    }

    fun recupereScore():Int{
        val prefs = this.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
        val connexionBDD = GestionBDD(this,DB_NAME,null,DB_VERSION)
        val score = connexionBDD.getScore(prefs.getString(KEY_NOM_PREFS,"CAMARADE"))
//Outils.logPerso("testScore",score.toString())
        return score
    }

    fun testFinPartie(tempsPartie:Long):Int{
        var checkPartie = 0
        val prefs = this.getSharedPreferences(MES_PREFS, MODE_PRIVATE)

        if(recupereScore()<0){
            checkPartie = 1
            return checkPartie
        } else if(prefs.getInt(KEY_NB_TACHES,0)>=5) {
            checkPartie = 2
            return checkPartie
        } else if(tempsPartie<=0){
            return checkPartie
        }

        return checkPartie
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun nombreDossier(){
        val prefs = this.getSharedPreferences(MES_PREFS, MODE_PRIVATE)
        when(prefs.getInt(KEY_NB_TACHES,0)){
            0-> boutonDossier.setBackgroundResource(R.drawable.dossier5)
            1-> boutonDossier.setBackgroundResource(R.drawable.dossier4)
            2-> boutonDossier.setBackgroundResource(R.drawable.dossier3)
            3-> boutonDossier.setBackgroundResource(R.drawable.dossier2)
            4-> boutonDossier.setBackgroundResource(R.drawable.dossier1)
        }
    }
}