package com.example.ctalutte;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Modifier;
import java.util.Timer;

import nl.dionsegijn.konfetti.compose.KonfettiViewKt;
import nl.dionsegijn.konfetti.core.Party;

public class CassePierreActivity extends AppCompatActivity {

    CountDownTimer compteur;
    public int decompte = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casse_pierre);
        
        //intégration du nom de la tâche
        TextView nomTache = (TextView) findViewById(R.id.nom_tache);
        nomTache.setText(R.string.tache_pierre);
        
        //intégration du score
        TextView scoreJoueur = (TextView)findViewById(R.id.score_joueur);
        scoreJoueur.setText("0");
        ImageButton boutonPierre = (ImageButton) findViewById(R.id.grosse_pierre);
        boutonPierre.setOnClickListener(new View.OnClickListener() {
            int score = 0;
            int total = Integer.valueOf(getString(R.string.score));
            @Override
            public void onClick(View v) {
                Outils.logPerso("Pierre", "entrée dans Click");
                if(score == total){
                    Outils.toastCourt(getApplicationContext(), "RALACHO TAVARICH !");
                    compteur.cancel();
                    finish();
                } else {
                score++;

                TextView scoreJoueur = (TextView)findViewById(R.id.score_joueur);
                scoreJoueur.setText(Integer.toString(score));
                Outils.logPerso("Pierre", "après incrémentation du score");
                Outils.logPerso("Pierre", String.valueOf(score));
                    Outils.logPerso("Pierre", String.valueOf(total));
                }
            }
        });

        //intégration du timer
        TextView chronoTache = (TextView) findViewById(R.id.chrono);
        compteur = new CountDownTimer(10000, 1000){
            public void onTick(long millisUntilFinished){
                chronoTache.setText(String.valueOf(decompte));
                decompte--;
            }
            public void onFinish(){
                Outils.toastCourt(getApplicationContext(), "Au GOULAG !");
                finish();
            }
        }.start();
        

    }
}