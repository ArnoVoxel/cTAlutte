package com.example.ctalutte;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class Connexion extends SQLiteOpenHelper {

    private static final String DB_NAME = "lutte";
    private static final Integer DB_VERSION = 1;
    private static final String TABLE_NAME = "camarades";
    private static final String NOM_JOUEUR = "nom_joueur";
    private static final String SCORE = "score";
    private static final String NB_TACHES = "nb_taches";
    private static final String TACHE_FINIE = "tache_finie";

    public Connexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version ) {
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


}
