import android.view.View
import com.example.ctalutte.Outils



import android.app.Activity
import android.content.Context
import android.os.CountDownTimer
import android.text.Layout
import android.widget.ImageView
import android.widget.TextView
import com.example.ctalutte.R

//
//
  class FragmentController  {
    var chronoTache:TextView?=null
    var compteur : CountDownTimer?=null
    var scoreJoueur : TextView?=null
    var nomTache : TextView?=null
//    var decompte : Int?=null
    var decompte : Long =10
    var context:Context?=null
    var activity:Activity?=null
//
//
//
//Timer
fun timer(time: Long) {

 compteur = object : CountDownTimer(time*1000, 1000) {
  override fun onTick(millisUntilFinished: Long) {
   chronoTache?.setText(decompte.toString())

      decompte--
  }

  override fun onFinish() {

      Outils.toastCourt(context, "Au GOULAG !")
      activity?.finish()
  }
 }.start()
}

    fun finTimer(){
        compteur?.cancel()
    }

fun affichage(score:Int){

 nomTache?.setText(R.string.tache_tract)
 scoreJoueur?.setText(score.toString())
}

}