package com.example.myapplication.ui.study

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.myapplication.R

class SetStudyEnd : AppCompatDialogFragment() {
    private var resultText: TextView? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var alertDialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater


        val view = inflater.inflate(R.layout.study_end, null)
        resultText = view.findViewById(R.id.result) as TextView
        val rightAnswers = requireArguments().getInt("rightAnswers")
        val wrongAnswers = requireArguments().getInt("wrongAnswers")
//        val wordList = requireArguments().getString("wordList")
        resultText!!.text = "You have $rightAnswers right answers and $wrongAnswers wrong"
        alertDialogBuilder.setView(view)
            .setTitle("Edit Set")
            .setPositiveButton("ok", DialogInterface.OnClickListener { _, _ ->
                dialog?.dismiss()
                (context as Activity).finish()
            })

        return alertDialogBuilder.create()


    }


}