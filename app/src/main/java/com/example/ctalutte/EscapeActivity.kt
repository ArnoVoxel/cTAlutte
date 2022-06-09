package com.example.ctalutte

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import pl.droidsonroids.gif.GifImageView

class EscapeActivity: AppCompatActivity() {

    var positionBonhomme = "bas"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escape)



        var buttonReturn = findViewById<Button>(R.id.retourEscape)
        buttonReturn.setOnClickListener(View.OnClickListener {
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

    }

    fun animateBackground(background: Int, animationXML: Int) {
        val animSlide = AnimationUtils.loadAnimation(this, animationXML)
        val vueBackground = findViewById<ImageView>(background)
        vueBackground.startAnimation(animSlide)
    }

    fun animateBonhomme(joueur: Int, animationXML: Int){
        /*ObjectAnimator.ofFloat(joueur, "translationY", 1000f).apply {
            duration = 500
            start()
            Outils.logPerso("animationEscape", "dans le objectanimator")
        }
        Outils.logPerso("animationEscape", "dehors le objectanimator")*/
        val animJump = AnimationUtils.loadAnimation(this, animationXML)
        val vueJoueur = findViewById<GifImageView>(joueur)
        positionBonhomme = "haut"
        vueJoueur.startAnimation(animJump)
    }
}