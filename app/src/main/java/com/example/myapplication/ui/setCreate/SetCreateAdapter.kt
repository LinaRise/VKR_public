package com.example.myapplication.ui.setCreate

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Word
import com.google.android.material.textfield.TextInputEditText


class SetCreateAdapter : RecyclerView.Adapter<SetCreateAdapter.SetCreateHolder>() {
    lateinit var context:Context

    var words:List<Word?> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetCreateHolder {
       val view = LayoutInflater.from(parent.context).inflate(
           R.layout.row_set_create_word,
           parent,
           false
       )
        return SetCreateHolder(view)
    }

    override fun onBindViewHolder(holder: SetCreateHolder, position: Int) {
        holder.original.setText(words[position]?.originalWord)
        holder.translated.setText(words[position]?.translatedWord)
    }

    override fun getItemCount(): Int {
       return words.size
    }

    fun setData(word: List<Word?>){
        this.words = word
        notifyDataSetChanged()
    }
   inner class SetCreateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var original: TextInputEditText = itemView.findViewById(R.id.original_input)
        var translated = itemView.findViewById(R.id.translated_input) as InstantAutoComplete

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
                    words[adapterPosition]!!.originalWord = original.text.toString()

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
                    words[adapterPosition]!!.translatedWord = translated.text.toString()
                }
                override fun afterTextChanged(editable: Editable) {}
            })
        }
    }

}