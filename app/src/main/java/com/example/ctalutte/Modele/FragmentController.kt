//import android.view.View
//import com.example.ctalutte.Outils
//
//
//
//import android.app.Activity
//import android.content.Context
//import android.os.CountDownTimer
//import android.text.Layout
//import android.widget.ImageView
//import android.widget.TextView
//import com.example.ctalutte.R
//
////
////
//  class FragmentController (var viewfrag:View ) {
//   private var chronoTache:TextView?=null
//   private var compteur : CountDownTimer?=null
//   private var scoreJoueur : TextView?=null
//   private var nomTache : TextView?=null
//    var decompte : Int?=null
//
////
////
////
////Timer
//fun timer(time: Long) {
//
// compteur = object : CountDownTimer(time*1000, 1000) {
//  override fun onTick(millisUntilFinished: Long) {
//   chronoTache?.setText(decompte.toString())
//   decompte!!--
//  }
//
//  override fun onFinish() {
//
//   Outils.toastCourt(applicationContext, "Au GOULAG !")
//   finish()
//  }
// }.start()
//}
//
//fun affichage(score:Int){
//
// nomTache?.setText(R.string.tache_tract)
// scoreJoueur?.setText(score.toString())
//}
//
//}