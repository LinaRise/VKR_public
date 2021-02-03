package com.example.myapplication.ui.setView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.setCreate.SetCreateActivity
import com.example.myapplication.ui.setCreate.SetCreateHolder

class SetViewAdapter(setViewActivity: SetViewActivity) : RecyclerView.Adapter<SetViewHolder>() {
    lateinit var context: Context
    private var words = emptyList<Word?>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_set_create_word, parent, false)
        return SetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        holder.original.setText(words[position]?.originalWord)
        holder.translated.setText(words[position]?.translatedWord)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun setData(word: List<Word>?) {
        if (word != null) {
            this.words = word
            notifyDataSetChanged()
        }

    }
}