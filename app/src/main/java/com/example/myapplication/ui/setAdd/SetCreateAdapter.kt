package com.example.myapplication.ui.setAdd

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Word

class SetCreateAdapter(setCreateActivity: SetCreateActivity, wordsList: ArrayList<Word>) : RecyclerView.Adapter<SetCreateHolder>() {
    lateinit var context:Context
    var words:ArrayList<Word> = wordsList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetCreateHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.row_set_create_word,null)
        return SetCreateHolder(view)
    }

    override fun onBindViewHolder(holder: SetCreateHolder, position: Int) {
        holder.original.setText(words[position].original)
        holder.translated.setText(words[position].translated)
    }

    override fun getItemCount(): Int {
       return words.size
    }
}