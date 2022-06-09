package com.example.ctalutte


import FragmentController
import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.DragEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.translationMatrix
import com.example.ctalutte.Modele.ManagerScore
import com.example.ctalutte.Modele.MyDragShadowBuilder
import pl.droidsonroids.gif.GifImageView


class TractActivity : AppCompatActivity() {

    //Quelques variables

    var score = 0
    var flag = false
    lateinit var compteur : CountDownTimer

    // constantes pour la connexion
    private val DB_NAME = "lutte"
    private val DB_VERSION = 1

    // constantes pour les sharedPreferences
    private val MES_PREFS = "dossier_camarade"
    private val KEY_NOM_PREFS = "nom_du_camarade"
    private val KEY_NB_TACHES = "nb_taches_finies"
    private val KEY_TEMPSCENTRALE = "key_temps_centrale"

    val tacheManager = ManagerScore(this)
    val animator = ObjectAnimator()
    val tempsActivity :Long = 10
    var decompte :Long = tempsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distrib)
        tacheManager.startTask()
        val tract = findViewById<ImageView>(R.id.drop_tract)
        val  frag = FragmentController( findViewById<TextView>(R.id.score_joueur),
                                        findViewById<TextView>(R.id.nom_tache),
                                        this)

        val chronoTache = findViewById<TextView>(R.id.chrono)
        compteur = object : CountDownTimer(decompte*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                chronoTache?.setText(decompte.toString())
                decompte--
            }
            override fun onFinish() {
                Outils.toastCourt(applicationContext, "Au GOULAG !")
                finish()
            }

            fun getDecompte(decompte: Int): Int {
                return decompte
            }

        }.start()

//       = frag.timer()
        frag.affichage(0)

        tract.apply {
            setOnTouchListener { view, motionEvent ->
                val item = ClipData.Item(view.tag as? CharSequence)

                val dragData = ClipData(
                    view.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item
                )
                val myShadow = MyDragShadowBuilder(this)
                view.startDragAndDrop(
                    dragData,
                    myShadow,
                    null,
                    0
                )
                true
            }
        }
        val poubListen = createListenerPoubelle(compteur,frag)
        val poubelle = findViewById<ImageView>(R.id.poubelle)
        poubelle.setOnDragListener(poubListen)

        val bonhommeListen = createListenerCamarade(compteur,frag)
        val bonhomme = findViewById<pl.droidsonroids.gif.GifImageView>(R.id.bonhomme)
        //Animation
        ObjectAnimator.ofFloat(bonhomme,"translationX",1000f).apply {
            duration = 5000
            Outils.logPerso("rotation","rotation 1")
            Outils.logPerso("rotation",bonhomme.getRotationY().toString())
            start()
        }
        ObjectAnimator.ofFloat(bonhomme,"rotationY",180f).apply {
            duration = 10
            startDelay=5000

            Outils.logPerso("rotation","rotation 2")
            Outils.logPerso("rotation",bonhomme.getRotationY().toString())

            start()
        }
        ObjectAnimator.ofFloat(bonhomme,"translationX",0f).apply {
            duration =5000
            startDelay = 5010
            Outils.logPerso("rotation","rotation 3")
            Outils.logPerso("rotation",bonhomme.getRotationY().toString())
            start()
        }
        bonhomme.setOnDragListener(bonhommeListen)
    }

//Event listener camarade :
    fun createListenerCamarade(compteur :CountDownTimer,frag :FragmentController) : View.OnDragListener {
        val camaradeListen = View.OnDragListener { v, event ->
        val receiverView: ImageView = v as ImageView
        val scoreJoueur = findViewById<TextView>(R.id.score_joueur)
        val total = Integer.valueOf(getString(R.string.score))
        val tacheManager = ManagerScore(this)

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
//                v.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION ->
                true
            DragEvent.ACTION_DRAG_EXITED -> {
//                v.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val item: ClipData.Item = event.clipData.getItemAt(0)
                score += 5
                scoreJoueur.setText(score.toString())
                if (score >= total) {
                    Outils.toastCourt(applicationContext, "VICTOIRE !")

                    // MAJ des infos en BDD
                    val prefs = getSharedPreferences(MES_PREFS, MODE_PRIVATE)
                    val prefsEditor = prefs.edit()
                    val nomCamarade = prefs.getString(KEY_NOM_PREFS, "CAMARADE")

                    //Gestion temps centrale
                    val tempsBureau = prefs.getLong(KEY_TEMPSCENTRALE,0)
                    val resultTemps = tempsBureau-((tempsActivity * 1000) - (decompte * 1000))

                    // retour au mainActivity
                    tacheManager.stopTask((score + decompte.toInt()),true)
                    var connexionBDD = GestionBDD(applicationContext, DB_NAME, null, DB_VERSION)
                    prefsEditor.putInt(KEY_NB_TACHES, connexionBDD.getNbTaches(nomCamarade))
                    prefsEditor.putLong(KEY_TEMPSCENTRALE,resultTemps)
                    prefsEditor.apply()
                    tacheManager.stopTask(score,true)
                    compteur?.cancel()
                    finish()
                }
                v.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                v.invalidate()
                true
            }
            else -> {
                false
            }
        }
        }
        return camaradeListen
}
    //Event Listener poubelle
    fun createListenerPoubelle(compteur :CountDownTimer, frag :FragmentController) : View.OnDragListener {
        val poubListen = View.OnDragListener {v, event ->
        val receiverView:ImageView = v as ImageView
        val scoreJoueur = findViewById<TextView>(R.id.score_joueur)
        val tacheManager = ManagerScore(this)
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {

                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                v.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION ->
                true
            DragEvent.ACTION_DRAG_EXITED -> {
                v.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val item: ClipData.Item = event.clipData.getItemAt(0)
                val prefs = getSharedPreferences(MES_PREFS, MODE_PRIVATE)
                val prefsEditor = prefs.edit()
                val tempsBureau = prefs.getLong(KEY_TEMPSCENTRALE,0)
                val resultTemps = tempsBureau-((tempsActivity * 1000) - (decompte * 1000))
                Outils.logPerso("TestCompteur","ResultTemps: " + resultTemps.toString() + " tempsActi: " + tempsActivity.toString() + " decompte: " + decompte.toString())
                var connexionBDD = GestionBDD(applicationContext, DB_NAME, null, DB_VERSION)
                var flag = false

                if(testRegard()){
                    score = 25 + decompte.toInt()
                    Outils.toastCourt(applicationContext, "VICTOIRE !")
                    // MAJ des infos en BDD

                    val nomCamarade = prefs.getString(KEY_NOM_PREFS, "CAMARADE")
                    flag = true

                    // retour au mainActivity

                    prefsEditor.putInt(KEY_NB_TACHES, connexionBDD.getNbTaches(nomCamarade))

                    }
                else{
                    score = -25
                    scoreJoueur.setText(score.toString())
                    Outils.toastLong(applicationContext,"TRICHEUR !!")


                }

                prefsEditor.putLong(KEY_TEMPSCENTRALE,resultTemps)
                prefsEditor.apply()
                tacheManager.stopTask(score,flag)
                compteur?.cancel()
                finish()
//                v.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
//                v.invalidate()
                true
            }
            else -> {
                false
            }
        }
        }
        return poubListen
    }

    fun testRegard(): Boolean{

        val bonhomme = findViewById<pl.droidsonroids.gif.GifImageView>(R.id.bonhomme)
        if(bonhomme.getRotationY().toString() == "180.0"){
            return true
        }else {
            return false;
        }

    }

    override fun onBackPressed(){
        //Gestion du score
        val scoreJoueur = findViewById<TextView>(R.id.score_joueur)
        score = -25
        scoreJoueur.setText(score.toString())

        //Gestion temps centrale
        val prefs = getSharedPreferences(MES_PREFS, MODE_PRIVATE)
        val prefsEditor = prefs.edit()
        val tempsBureau = prefs.getLong(KEY_TEMPSCENTRALE,0)
        val resultTemps = tempsBureau-decompte
        prefsEditor.putLong(KEY_TEMPSCENTRALE,resultTemps)
        prefsEditor.apply()

        //Retour au bureauActivity
        Outils.toastLong(applicationContext,"La fuite est une insulte au Parti !!")
        tacheManager.stopTask(score,false)
        compteur.cancel()
        finish()

    }
}

