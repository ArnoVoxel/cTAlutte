package com.example.ctalutte;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.constraintlayout.motion.widget.Debug;

public class Outils {
    private static Boolean DEBUG = true;

    public void logPerso(String activite, String message){
        if(DEBUG){
        Log.d(activite, message);
        }
    }

    public void toastCourt(Context context, String message){
        if(DEBUG){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void toastLong(Context context, String message){
        if(DEBUG){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
