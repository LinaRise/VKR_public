package com.example.myapplication.ui.setCreate

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText

/*
class SetCreateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var original: TextInputEditText = itemView.findViewById(R.id.original_input)
    var translated: TextInputEditText = itemView.findViewById(R.id.translated_input)

    init {
        original.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                words[adapterPosition]!!.originalWord = original_input.text.toString()

            }

            override fun afterTextChanged(editable: Editable) {}
        })

        translated.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                words[adapterPosition]!!.translatedWord = translated_input.text.toString()
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }
}*/
