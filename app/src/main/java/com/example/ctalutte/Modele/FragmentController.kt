import android.view.View
import com.example.ctalutte.Outils



import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.os.CountDownTimer
import android.text.Layout
import android.widget.ImageView
import android.widget.TextView
import com.example.ctalutte.R

//
//
 class FragmentController constructor(var chronoTache : TextView,
                                      var compteur : CountDownTimer?=null,
                                      var scoreJoueur : TextView,
                                      var nomTache : TextView,
                                      var decompte : Long =10,
                                      var context:Context,
                                      var activity:Activity)  {

//Timer
fun timer(): CountDownTimer {
//var compteur = getCompteur()
 compteur = object : CountDownTimer(decompte*1000, 1000) {
      override fun onTick(millisUntilFinished: Long) {
           chronoTache?.setText(decompte.toString())
           decompte--
      }
      override fun onFinish() {
          Outils.toastCourt(context, "Au GOULAG !")
          activity?.finish()
      }
     }.start()
    return compteur as CountDownTimer
}

    fun finTimer(){
        compteur?.cancel()
    }

fun affichage(score:Int){

 nomTache?.setText(R.string.tache_tract)
 scoreJoueur?.setText(score.toString())
}

}