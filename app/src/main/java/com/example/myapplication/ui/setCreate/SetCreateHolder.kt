package com.example.myapplication.ui.setCreate

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText

class SetCreateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var original: TextInputEditText = itemView.findViewById(R.id.original_input)
    var translated: TextInputEditText = itemView.findViewById(R.id.translated_input)

}