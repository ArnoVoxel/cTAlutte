package com.example.ctalutte

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView

class EscapeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escape)

        animateBackground()

    }

    fun animateBackground(){
        val vue1 = findViewById<ImageView>(R.id.backgroundEscape1)
        vue1.translationX = 2000f
        vue1.animate().translationX(-1000f).setDuration(4000).start()
    }
}

