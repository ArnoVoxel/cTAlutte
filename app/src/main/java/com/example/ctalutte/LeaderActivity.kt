package com.example.ctalutte

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LeaderActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader)

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
//        val connexionBDD = GestionBDD(this, DB_NAME, null, DB_VERSION)
//        val prefs = getSharedPreferences(MES_PREFS, MODE_PRIVATE)
//        val prefsEditor = prefs.edit()
////        connexionBDD.setTempsCentrale(prefs.getString(KEY_NOM_PREFS,"CAMARADE"),(decompteCentrale*1000).toInt())
//        compteur.cancel()
        startActivity(intent)
    }
}