package com.example.ctalutte;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class GestionBDD extends SQLiteOpenHelper {

    private static final String DB_NAME = "lutte";
    private static final Integer DB_VERSION = 1;
    private static final String TABLE_NAME = "camarades";
    private static final String NOM_JOUEUR = "nom_joueur";
    private static final String SCORE = "score";
    private static final String NB_TACHES = "nb_taches";
    private static final String TACHE_FINIE = "tache_finie";

    public GestionBDD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version ) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " +TABLE_NAME+" ("
                +NOM_JOUEUR+" TEXT PRIMARY KEY, "
                +SCORE+" int NOT NULL, "
                +TACHE_FINIE+" TEXT NOT NULL, "
                +NB_TACHES+" int NOT NULL )";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ TABLE_NAME);
        onCreate(db);
    }

    public SQLiteDatabase getReadableDatabase(){
        return super.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase(){
        return super.getWritableDatabase();
    }

    public void ajouterCamarade(String nomCmarade, Integer scoreCamarade, Integer nbTachesCamarade, String tacheFinie){
        Outils.logPerso("BDD","début fct ajouter");
        SQLiteDatabase db = getReadableDatabase();
        Outils.logPerso("BDD","après getReadable");
        scoreCamarade = 0;
        nbTachesCamarade = 0;

        String insertCamarade ="INSERT INTO "+TABLE_NAME+" ("+NOM_JOUEUR+", "+SCORE+", "+NB_TACHES+","+TACHE_FINIE+") \n VALUES('"+nomCmarade+"', "+scoreCamarade+", "+nbTachesCamarade+", '"+tacheFinie+"');";
        Outils.logPerso("BDD","après création de la string de requête");
        Outils.logPerso("BDD","requête : "+insertCamarade);

        db.execSQL(insertCamarade);
        Outils.logPerso("BDD","après insert en BDD");
        db.close();
    }

    public void commencerTache(String nomCamarade){
        Outils.logPerso("modifierBDD", "début fct ternimerTache");
        SQLiteDatabase db = getReadableDatabase();
        Outils.logPerso("modifierBDD","après getReadable");

        String modifierTache = "UPDATE "+TABLE_NAME+" SET "+ TACHE_FINIE+" = \'non\' WHERE "+ NOM_JOUEUR+" = "+ nomCamarade +";";
        Outils.logPerso("modifierBDD","après préparation de la requête");
        Outils.logPerso("modifierBDD",modifierTache);

        db.execSQL(modifierTache);
        Outils.logPerso("modifierBDD","après exécution de la requête");
    }

    public void terminerTache(String nomCamarade, int nouveauScore){
        Outils.logPerso("modifierBDD", "début fct ternimerTache");
        SQLiteDatabase db = getReadableDatabase();
        Outils.logPerso("modifierBDD","après getReadable");

        String modifierTache = "UPDATE "+TABLE_NAME+" SET "+ TACHE_FINIE+" = \'oui\' WHERE "+ NOM_JOUEUR+" = "+ nomCamarade +";";
        Outils.logPerso("modifierBDD","après préparation de la requête");
        Outils.logPerso("modifierBDD",modifierTache);

        db.execSQL(modifierTache);
        Outils.logPerso("modifierBDD","après tâche finie passée à oui");
        modifierScoreNbTache(nomCamarade, nouveauScore);
        Outils.logPerso("modifierBDD","après nouveau Score et nb tâches incrémenté");
    }

    public void modifierScoreNbTache(String nomCamarade, int nouveauScore){
        SQLiteDatabase db = getReadableDatabase();

        String nouveauScoreNbTache = "UPDATE "+TABLE_NAME+" SET "+SCORE+" = "+SCORE+" +" +nouveauScore+" ,"+ NB_TACHES+" = "+NB_TACHES+" +1\n" +
                "WHERE "+NOM_JOUEUR+" = "+ nomCamarade+";";

        db.execSQL(nouveauScoreNbTache);
    }


}
