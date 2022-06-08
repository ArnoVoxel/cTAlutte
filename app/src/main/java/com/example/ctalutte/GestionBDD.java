package com.example.ctalutte;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    public List<CamaradeInfos> getListeInfosCamarades(){
        List<CamaradeInfos> listeCamarades = new ArrayList<CamaradeInfos>();

        SQLiteDatabase db = getReadableDatabase();

        String requeteListe = "SELECT * FROM "+ TABLE_NAME;
        Outils.logPerso("BDD", requeteListe);

        Cursor cursor = db.rawQuery(requeteListe, null);

        if(cursor.moveToFirst()){
            do {
                CamaradeInfos camarade = new CamaradeInfos();
                camarade.setNom_joueur(cursor.getString(0));
                camarade.setScore(cursor.getInt(1));
                camarade.setTache_finie(cursor.getString(2));
                camarade.setNb_taches(cursor.getInt(3));

                listeCamarades.add(camarade);
            }while (cursor.moveToNext());
        }

        return listeCamarades;
    }

    public List<CamaradeInfos> getListeNomsCamarades(){
        List<CamaradeInfos> listeNomsCamarades = new ArrayList<CamaradeInfos>();

        SQLiteDatabase db = getReadableDatabase();

        String requeteNoms = "SELECT nom_joueur FROM "+ TABLE_NAME+";";

        Cursor cursor = db.rawQuery(requeteNoms, null);

        if(cursor.moveToFirst()){
            do{
                CamaradeInfos camarade = new CamaradeInfos();
                camarade.setNom_joueur(cursor.getString(0));

                listeNomsCamarades.add(camarade);
            }while(cursor.moveToNext());
        }
        return listeNomsCamarades;
    }

    public Integer getNbTaches(String nomCamarade){
        Integer nbTaches = 0;

        SQLiteDatabase db = getReadableDatabase();
        String requeteTaches = "SELECT nb_taches FROM "+ TABLE_NAME+" WHERE nom_joueur = '"+ nomCamarade +"';";

        Cursor cursor = db.rawQuery(requeteTaches, null);

        if (cursor != null)
            cursor.moveToFirst();

        nbTaches = cursor.getInt(0);

        return nbTaches;
    }


    public void ajouterCamarade(String nomCmarade, Integer scoreCamarade, Integer nbTachesCamarade, String tacheFinie){

        SQLiteDatabase db = getReadableDatabase();

        scoreCamarade = 0;
        nbTachesCamarade = 0;

        String insertCamarade ="INSERT INTO "+TABLE_NAME+" ("+NOM_JOUEUR+", "+SCORE+", "+NB_TACHES+","+TACHE_FINIE+") \n VALUES('"+nomCmarade+"', "+scoreCamarade+", "+nbTachesCamarade+", '"+tacheFinie+"');";

        db.execSQL(insertCamarade);
        db.close();
    }

    public void commencerTache(String nomCamarade){
        Outils.logPerso("modifierBDD", "début fct commencer Tâche");
        SQLiteDatabase db = getReadableDatabase();
        Outils.logPerso("modifierBDD","après getReadable");

        String modifierTache = "UPDATE "+TABLE_NAME+" SET "+ TACHE_FINIE+" = \'non\' WHERE "+ NOM_JOUEUR+" = '"+ nomCamarade +"';";
        Outils.logPerso("modifierBDD","après préparation de la requête");
        Outils.logPerso("modifierBDD",modifierTache);

        db.execSQL(modifierTache);
        db.close();
        Outils.logPerso("modifierBDD","après exécution de la requête");
    }

    public void terminerTache(String nomCamarade, int nouveauScore, boolean etatPartie){
        Outils.logPerso("modifierBDD", "début fct terminerTache");
        SQLiteDatabase db = getReadableDatabase();
        Outils.logPerso("modifierBDD","après getReadable");

        String modifierTache = "UPDATE "+TABLE_NAME+" SET "+ TACHE_FINIE+" = \'oui\' WHERE "+ NOM_JOUEUR+" = '"+ nomCamarade +"';";
        Outils.logPerso("modifierBDD","après préparation de la requête");
        Outils.logPerso("modifierBDD",modifierTache);

        db.execSQL(modifierTache);
        Outils.logPerso("modifierBDD","après tâche finie passée à oui");
        modifierScoreNbTache(nomCamarade, nouveauScore,etatPartie);
        Outils.logPerso("modifierBDD","après nouveau Score et nb tâches incrémenté");
    }

    public void modifierScoreNbTache(String nomCamarade, int nouveauScore, boolean etatPartie){
        SQLiteDatabase db = getReadableDatabase();
        String nouveauScoreNbTache="";
        if(etatPartie) {
             nouveauScoreNbTache = "UPDATE " + TABLE_NAME + " SET " + SCORE + " = " + SCORE + " +'" + nouveauScore + "' ," + NB_TACHES + " = " + NB_TACHES + " +1\n" +
                    "WHERE " + NOM_JOUEUR + " = '" + nomCamarade + "';";
        }else{
             nouveauScoreNbTache = "UPDATE " + TABLE_NAME + " SET " + SCORE + " = " + SCORE + " +'" + nouveauScore + "' WHERE " + NOM_JOUEUR + " = '" + nomCamarade + "';";
        }
        db.execSQL(nouveauScoreNbTache);
        db.close();
    }




}