package com.example.ctalutte

//import FragmentController
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.DragEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ctalutte.Modele.MyDragShadowBuilder
import kotlin.CharSequence
import kotlin.Long
import kotlin.Suppress
import kotlin.apply
import kotlin.arrayOf




class TractActivity : AppCompatActivity() {

    //Quelques variables
//    var Frag : FragmentController?=null
    var score = 0
    var compteur : CountDownTimer?=null
    var scoreJoueur : TextView?=null
    var nomTache : TextView?=null



    public var decompte =10
    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distrib)
        val tract = findViewById<ImageView>(R.id.drop_tract)
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
        timer(20)
        affichage(0)
        var poubelle = findViewById<ImageView>(R.id.poubelle)
        poubelle.setOnDragListener(poubListen)
        var bonhomme = findViewById<pl.droidsonroids.gif.GifImageView>(R.id.bonhomme)
        bonhomme.setOnDragListener(camaradeListen)
    }

//Event listener camarade :
    private val camaradeListen = View.OnDragListener {v, event ->
        val receiverView:ImageView = v as ImageView
        val scoreJoueur = findViewById<TextView>(R.id.score_joueur)
        val total = Integer.valueOf(getString(R.string.score))
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                receiverView.setBackgroundColor(Color.RED)
                v.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION ->
                true
            DragEvent.ACTION_DRAG_EXITED -> {
                receiverView.setBackgroundColor(Color.GREEN)
                v.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val item: ClipData.Item = event.clipData.getItemAt(0)
                receiverView.setBackgroundColor(Color.BLACK)
                score += 5
                scoreJoueur.setText(score.toString())
                if(score == total){
                    Outils.toastCourt(applicationContext,"VICTOIRE !")
                    compteur?.cancel()
                    finish()
                }
                v.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                receiverView.setBackgroundColor(Color.DKGRAY)
                v.invalidate()
                when(event.result) {
                    true ->
                        receiverView.setBackgroundColor(Color.DKGRAY)
                    else ->{
                        receiverView.setBackgroundColor(Color.GREEN)
                    }
                }
                true
            }
            else -> {
                false
            }
        }
    }
    //Event Listener poubelle
    private val poubListen = View.OnDragListener {v, event ->
        val receiverView:ImageView = v as ImageView
        val scoreJoueur = findViewById<TextView>(R.id.score_joueur)
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {

                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                receiverView.setBackgroundColor(Color.BLUE)
                v.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION ->
                true
            DragEvent.ACTION_DRAG_EXITED -> {
                receiverView.setBackgroundColor(Color.BLACK)
                v.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val item: ClipData.Item = event.clipData.getItemAt(0)
                if(testRegard()){
                    score +=10
                    scoreJoueur.setText(score.toString())}
                else{
                    score = -10
                    scoreJoueur.setText(score.toString())
                    Outils.toastLong(applicationContext,"TRICHEUR !!")
                    compteur?.cancel()
                    finish()
                }
                receiverView.setBackgroundColor(Color.CYAN)
                v.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                receiverView.setBackgroundColor(Color.DKGRAY)
                v.invalidate()
                when(event.result) {
                    true ->
                        receiverView.setBackgroundColor(Color.DKGRAY)
                    else ->{
                        receiverView.setBackgroundColor(Color.GREEN)
                    }
                }
                true
            }
            else -> {
                false
            }
        }
    }

    fun testRegard(): Boolean{
        return false;
    }

    //Timer
    fun timer(time: Long) {
    var chronoTache = findViewById<TextView>(R.id.chrono)
     compteur = object : CountDownTimer(time*1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            chronoTache.setText(decompte.toString())
            decompte--
        }

        override fun onFinish() {
            Outils.toastCourt(applicationContext, "Au GOULAG !")
            finish()
        }
    }.start()
    }

    fun affichage(score:Int){
        nomTache = findViewById<TextView>(R.id.nom_tache)
        nomTache?.setText(R.string.tache_tract)
        scoreJoueur = findViewById<TextView>(R.id.score_joueur)
        scoreJoueur?.setText(score.toString())
    }
}
