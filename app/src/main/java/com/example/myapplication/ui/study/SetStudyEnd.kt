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
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setCancelable(false)

        val inflater = requireActivity().layoutInflater


        val view = inflater.inflate(R.layout.study_end, null)
        resultText = view.findViewById(R.id.result) as TextView
        val rightAnswers = requireArguments().getInt("rightAnswers")
        val wrongAnswers = requireArguments().getInt("wrongAnswers")
//        val wordList = requireArguments().getString("wordList")
        resultText!!.text = getString(R.string.you_have)+" "+rightAnswers +" "+getString(R.string.right_answers_and)+" "+wrongAnswers +" "+ getString(
                    R.string.wrong)
        alertDialogBuilder.setView(view)
            .setTitle(getString(R.string.result))
            .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { _, _ ->
                dialog?.dismiss()
                (context as Activity).finish()
            })
        return alertDialogBuilder.create()


    }


}