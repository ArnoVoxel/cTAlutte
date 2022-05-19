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
import android.content.Context
import android.widget.Button
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
    private lateinit var startMenuLayout: View

    fun validerAdhesion(){


            startMenuLayout = findViewById(R.id.modal_accueil)
            startMenuLayout.visibility = View.GONE
            // abonnement du bouton pour lancer la musique
            var boutonRadio = findViewById<ImageButton>(androidx.appcompat.R.id.radio);
            boutonRadio.setOnClickListener(View.OnClickListener {
                var chanson = MediaPlayer.create(this, R.raw.the_internationale_english)
                chanson?.start()
            })

            //abonnement bouton dossier
        var boutonDossier = findViewById<ImageButton>(R.id.bouton_dossier)
        boutonDossier.setOnClickListener(View.OnClickListener {
            Outils.toastCourt(this, "Tu vas bosser fidèle Camarade")
        })
    }

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


        var boutonAdhesion = findViewById<Button>(R.id.bouton_valider_modal)
        boutonAdhesion.setOnClickListener(View.OnClickListener {
            validerAdhesion()
        })
        //Création de la modale

        /*var popup = StartMenuKotlin()
        popup.show(supportFragmentManager, "popo")*/




    }


}