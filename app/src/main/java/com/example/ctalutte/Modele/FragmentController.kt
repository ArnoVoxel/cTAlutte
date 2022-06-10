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
 class FragmentController constructor(var scoreJoueur : TextView,
                                      var nomTache : TextView,
                                      var context:Context,) {

fun affichage(score:Int){

 nomTache?.setText(R.string.tache_tract)
 scoreJoueur?.setText(score.toString())
}



}