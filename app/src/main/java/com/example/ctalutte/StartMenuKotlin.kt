package com.example.ctalutte

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Debug
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar

class StartMenuKotlin : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.message_accueil)
                .setPositiveButton(R.string.bouton_valider,
                    DialogInterface.OnClickListener { dialog, id ->
                        Toast.makeText(it, "VALIDE", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton(R.string.bouton_refuser,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
