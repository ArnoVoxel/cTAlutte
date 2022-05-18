package com.example.ctalutte

import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import android.app.FragmentManager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //Affichage en plein écran + suprresion barre status
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Création de la modale
//        setContentView(R.layout.menu_fragment)
 /*       Log.d("PLOP","Avant crea vue")

        Log.d("PLOP","Apres crea vue")
        val accueilFragment:AccueilFragment= AccueilFragment.getInstance()
        Log.d("PLOP","Apres getinstance")
        accueilFragment.show(supportFragmentManager,AccueilFragment.TAG)
        Log.d("PLOP","Apres show")*/



        var boutonRadio = findViewById<ImageButton>(androidx.appcompat.R.id.radio);
        boutonRadio.setOnClickListener(View.OnClickListener {
            var chanson = MediaPlayer.create(this, R.raw.the_internationale_english)
            chanson?.start()
        })

    }


}