package com.example.ctalutte

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ctalutte.Modele.ManagerScore
import pl.droidsonroids.gif.GifImageView

class EscapeActivity: AppCompatActivity() {

    // constantes pour la connexion
    private val DB_NAME = "lutte"
    private val DB_VERSION = 1

    // constantes pour les sharedPreferences
    private val MES_PREFS = "dossier_camarade"
    private val KEY_NOM_PREFS = "nom_du_camarade"
    private val KEY_NB_TACHES = "nb_taches_finies"

    var positionBonhomme = "bas"
    var positionObstacle : String = "bas"
    var decompte : Int = 10
    val tacheManager = ManagerScore(this)
    var score : Int = 0
    val total : Int = R.string.score.toString().toInt()
    var valeurObstacle :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escape)

        tacheManager.startTask()

        // intégration du nom de la tâche
        val nomTache = findViewById<TextView>(R.id.nom_tache)
        nomTache.setText(R.string.tache_running)

        // intégration du score
        val scoreJoueur = findViewById<TextView>(R.id.score_joueur)
        scoreJoueur.setText("0")

        // intégration du total
        val scoreTotal = findViewById<TextView>(R.id.total_tache)
        scoreTotal.setText("50")

        // intégration du timer
        val chronoTache = findViewById<TextView>(R.id.chrono)

        val compteur = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                chronoTache.setText(decompte.toString())
                decompte--

                Outils.logPerso("positionObstacle", positionObstacle)
                if(positionObstacle != positionBonhomme){
                    score += 10
                    scoreJoueur.setText(score.toString())
                }

                if (score >= 50){
                    Outils.toastCourt(getApplicationContext(), "RALACHO TAVARICH !")
/*                    // RAZ des compteurs
                    compteur.cancel()
                    compteurObstacle.cancel()*/

                    // MAJ des infos en BDD

                    // MAJ des infos en BDD
                    val connexionBDD = GestionBDD(applicationContext, DB_NAME, null, DB_VERSION)
                    val prefs = getSharedPreferences(MES_PREFS, MODE_PRIVATE)
                    val prefsEditor = prefs.edit()
                    val nomCamarade = prefs.getString(KEY_NOM_PREFS, "CAMARADE")

                    // retour au mainActivity
                    tacheManager.stopTask(score + decompte, true)
                    prefsEditor.putInt(KEY_NB_TACHES, connexionBDD.getNbTaches(nomCamarade))
                    prefsEditor.commit()

                    this.cancel()
                    finish()
                }
            }

            override fun onFinish() {
                Outils.toastCourt(applicationContext, "Au GOULAG !")
                tacheManager.stopTask(0,false)
                this.cancel()
                finish()
            }
        }
        compteur.start()

        // timer pour création des obstacles
        val compteurObstacle = object : CountDownTimer(10000, 2500) {

            override fun onTick(millisUntilFinished: Long) {
                valeurObstacle = (1..5).random()
                createObstacle(valeurObstacle)
                positionObstacle = createObstacle(valeurObstacle)
            }

            override fun onFinish() {
                this.cancel()
                finish()
            }
        }
        compteurObstacle.start()

        var escapeBonhomme = findViewById<GifImageView>(R.id.escape_bonhomme)
        escapeBonhomme.setOnClickListener(View.OnClickListener {
            if(positionBonhomme.equals("bas")){
            animateBonhomme(R.id.escape_bonhomme, R.anim.escape_monte)
                positionBonhomme = "haut"
            } else {
                animateBonhomme(R.id.escape_bonhomme, R.anim.escape_descend)
                positionBonhomme = "bas"
            }
        })

        animateBackground(R.id.backgroundEscape1, R.anim.escape_background_animation1)
        animateBackground(R.id.backgroundEscape2, R.anim.escape_background_animation2)

    }

    fun animateBackground(background: Int, animationXML: Int) {
        val animSlide = AnimationUtils.loadAnimation(this, animationXML)
        val vueBackground = findViewById<ImageView>(background)
        vueBackground.startAnimation(animSlide)
    }

    fun animateBonhomme(joueur: Int, animationXML: Int){
        val animJump = AnimationUtils.loadAnimation(this, animationXML)
        val vueJoueur = findViewById<GifImageView>(joueur)
        positionBonhomme = "haut"
        vueJoueur.startAnimation(animJump)
    }

    fun animateObstacle(animationXML: Int){
        val animObstacle = AnimationUtils.loadAnimation(this, animationXML)
        val vueObstacle = findViewById<LinearLayout>(R.id.obstacle_layout)
        vueObstacle.startAnimation(animObstacle)
    }

    fun createObstacle(compteur: Int): String {
        var positionObstacle : String
        // création des obstacles
        val mainLayout = findViewById<LinearLayout>(R.id.obstacle_layout)
        var obstacleVue = layoutInflater.inflate(R.layout.activity_escape_obstacles, mainLayout, false)

        // animation suivant le random du timer
        mainLayout.addView(obstacleVue)
        if((compteur % 2) == 0) {
            animateObstacle(R.anim.escape_obstacle_haut)
            positionObstacle = "haut"
        } else {
            animateObstacle(R.anim.escape_obstacle_bas)
            positionObstacle = "bas"
        }
        return positionObstacle
    }
}