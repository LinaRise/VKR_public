package com.example.myapplication.ui.setCreate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Word

class SetCreateAdapter(setCreateActivity: SetCreateActivity) : RecyclerView.Adapter<SetCreateHolder>() {
    lateinit var context:Context
    private var words = emptyList<Word?>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetCreateHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.row_set_create_word,parent,false)
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
}