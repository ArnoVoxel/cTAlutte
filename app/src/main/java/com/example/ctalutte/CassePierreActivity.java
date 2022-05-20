package com.example.ctalutte;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.github.jinatonic.confetti.CommonConfetti;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import java.lang.reflect.Modifier;
import java.text.AttributedCharacterIterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import nl.dionsegijn.konfetti.compose.KonfettiViewKt;

public class CassePierreActivity extends AppCompatActivity {

    CountDownTimer compteur;
    public int decompte = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casse_pierre);

        //animation de la pierre
        View pierreShake = findViewById(R.id.image_pierre);
        final SpringAnimation animation = new SpringAnimation(pierreShake, SpringAnimation.ROTATION, 5f);
        animation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        animation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                pierreShake.setRotation(0f);
                animation.cancel();
            }
        });

        //position du touch pour particules
        View pierreEcran = findViewById(R.id.pierre_ecran);
        final int[] valeurX = {0};
        final int[] valeurY = {0};

        pierreEcran.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        if(action == MotionEvent.ACTION_DOWN){
                            valeurX[0] = MotionEvent.AXIS_X;
                            Outils.logPerso("Pierre", "onTouch position X "+String.valueOf(MotionEvent.AXIS_X));
                            valeurY[0] = MotionEvent.AXIS_Y;
                        }

                        return true;
                    }
                }
        );

        //intégration du nom de la tâche
        TextView nomTache = (TextView) findViewById(R.id.nom_tache);
        nomTache.setText(R.string.tache_pierre);
        
        //intégration du score
        TextView scoreJoueur = (TextView)findViewById(R.id.score_joueur);
        scoreJoueur.setText("0");
        Button boutonPierre = (Button) findViewById(R.id.grosse_pierre);
        boutonPierre.setOnClickListener(new View.OnClickListener() {
            //initiliasation des variables
            int score = 0;
            int total = Integer.valueOf(getString(R.string.score));

            @Override
            public void onClick(View v) {
                animation.cancel();

                if(score == total){
                    Outils.toastCourt(getApplicationContext(), "RALACHO TAVARICH !");
                    compteur.cancel();
                    finish();
                } else {
                    score++;
                    TextView scoreJoueur = (TextView)findViewById(R.id.score_joueur);
                    scoreJoueur.setText(Integer.toString(score));


                    animation.start();


                    //particules
                    ViewGroup layoutPierres = (ViewGroup)findViewById(R.id.layout_pierres).getParent();
                    CommonConfetti.rainingConfetti(layoutPierres, new int[] {Color.BLACK}).oneShot();
                    //CommonConfetti.explosion(layoutPierres, 1000, 1000, new int[] {Color.BLACK});

                    Outils.logPerso("Pierre", "touch position X "+ MotionEvent.AXIS_X);
                    Outils.logPerso("Pierre", "touch position Y "+ valeurY[0]);

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

