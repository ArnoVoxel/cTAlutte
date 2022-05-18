package com.example.ctalutte

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment


class ModalAccueilFragment:DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.message_accueil))
            .setPositiveButton(getString(R.string.bouton_valider)) {_,_ -> }
            .create()

        companion object{
            const val TAG = "AccueilEnFanfare"
        }
    //ModalAccueilFragment().show(childFragmentManager, ModalAccueilFragment.TAG)
}