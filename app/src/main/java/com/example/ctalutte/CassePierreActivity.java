package com.example.ctalutte;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ctalutte.Modele.ManagerScore;
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
    public int score = 0;

    // constantes pour la connexion
    private final String DB_NAME = "lutte";
    private final Integer DB_VERSION = 1;

    // constantes pour les sharedPreferences
    private final String MES_PREFS = "dossier_camarade";
    private final String KEY_NOM_PREFS = "nom_du_camarade";
    private final String KEY_NB_TACHES = "nb_taches_finies";
    private final String KEY_TEMPSCENTRALE = "key_temps_centrale";

    final ManagerScore tacheManager = new ManagerScore(this);
    final Long tempsActivity =10L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casse_pierre);

        Outils.logPerso("modifierBDD", "activité pierres");

        Outils.logPerso("taskManager", "avant startTask");
        tacheManager.startTask();
        Outils.logPerso("taskManager", "après startTask");

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
                    boutonPierre.setOnClickListener(null);
                    score = score + decompte;
                    backToOffice(true);
                    // RAZ du décompte de la tâche
        /*            compteur.cancel();

                    // MAJ des infos en BDD
                    GestionBDD connexionBDD = new GestionBDD(getApplicationContext(), DB_NAME, null, DB_VERSION);
                    SharedPreferences prefs = getSharedPreferences(MES_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    String nomCamarade = prefs.getString(KEY_NOM_PREFS, "CAMARADE");

                    // retour au mainActivity
                    tacheManager.stopTask((score+decompte),true);
                    prefsEditor.putInt(KEY_NB_TACHES, connexionBDD.getNbTaches(nomCamarade));
                    prefsEditor.commit();

                    finish();*/
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
        compteur = new CountDownTimer(tempsActivity*1000, 1000){
            public void onTick(long millisUntilFinished){
                chronoTache.setText(String.valueOf(decompte));
                decompte--;
            }
            public void onFinish(){
                Outils.toastCourt(getApplicationContext(), "Au GOULAG !");
                score = -25;
                backToOffice(false);
//                tacheManager.stopTask(0,false);
//                finish();
            }
        }.start();

    }

    @Override
    public void onBackPressed(){
        Outils.toastCourt(getApplicationContext(), "Au GOULAG !");
        score = -25;
        backToOffice(false);
//        compteur.cancel();
//        tacheManager.stopTask(-25,false);
//        finish();
    }

    public void backToOffice (Boolean flagVictoire){
       SharedPreferences prefs= getSharedPreferences(MES_PREFS,MODE_PRIVATE);
       SharedPreferences.Editor prefsEditor = prefs.edit();
       Long tempsBureau = prefs.getLong(KEY_TEMPSCENTRALE,0L);
       Long tempsDansJeu = (tempsBureau - tempsBureau)*1000;
       Integer resultTemps = Math.toIntExact(tempsBureau - tempsDansJeu);
       String nomCamarade = prefs.getString(KEY_NOM_PREFS,"CAMARADE");
       tacheManager.stopTask(score,flagVictoire,resultTemps);
       GestionBDD connexionBDD= new GestionBDD(this,DB_NAME,null,DB_VERSION);
       if(flagVictoire){
           prefsEditor.putInt(KEY_NB_TACHES, connexionBDD.getNbTaches(nomCamarade));
       }
        prefsEditor.putLong(KEY_TEMPSCENTRALE,resultTemps);
        prefsEditor.apply();
        compteur.cancel();
        finish();
    }
}