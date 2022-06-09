package com.example.ctalutte

import FragmentController
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
import pl.droidsonroids.gif.GifImageView

class EscapeActivity: AppCompatActivity() {

    var positionBonhomme = "bas"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escape)

        val  frag = FragmentController(findViewById<TextView>(R.id.chrono),
            null,
            findViewById<TextView>(R.id.score_joueur),
            findViewById<TextView>(R.id.nom_tache),
            10,
            this,
            this)

        val compteur = object : CountDownTimer(5000, 1500) {
            var valeurObstacle = 0
            override fun onTick(millisUntilFinished: Long) {
                valeurObstacle += 1500
                createObstacle(valeurObstacle)
            }

            override fun onFinish() {
                cancel()
                finish()
            }
        }
        compteur.start()

        var buttonReturn = findViewById<Button>(R.id.retourEscape)
        buttonReturn.setOnClickListener(View.OnClickListener {
            compteur.cancel()
            val intent = Intent(this, BureauActivity::class.java)
            startActivity(intent)
        })

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

        //animateObstacle(R.id.obstacle_layout, R.anim.escape_obstacle_bas)
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

    fun createObstacle(compteur: Int){
        /*var obstacle : Int = R.layout.activity_escape_obstacles
        setContentView(obstacle)*/

        // création des obstacles
        val mainLayout = findViewById<LinearLayout>(R.id.obstacle_layout)
        var obstacleVue = layoutInflater.inflate(R.layout.activity_escape_obstacles, mainLayout, false)

        mainLayout.addView(obstacleVue)
        if(compteur> 2500) {
            animateObstacle(R.anim.escape_obstacle_haut)
        } else {
            animateObstacle(R.anim.escape_obstacle_bas)
        }
    }
}