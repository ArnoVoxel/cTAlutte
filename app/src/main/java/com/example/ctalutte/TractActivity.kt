package com.example.ctalutte

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.os.PersistableBundle
import android.view.DragEvent
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TractActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distrib)
        var tract = findViewById<ImageView>(R.id.drop_tract)
        tract.apply {
            setOnTouchListener {  view, motionEvent ->
                val item = ClipData.Item(view.tag as? CharSequence)

                val dragData = ClipData(
                    view.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),item)

                val myShadow = MyDragShadowBuilder(this)
                view.startDragAndDrop(dragData,
                    myShadow,
                    null,
                    0)
                true}
           /* setOnLongClickListener { view ->
                val item = ClipData.Item(view.tag as? CharSequence)

                val dragData = ClipData(
                    view.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),item)

                val myShadow = MyDragShadowBuilder(this)
                view.startDragAndDrop(dragData,
                                    myShadow,
                                    null,
                                                0)
                true
            }*/
        }

    }
}