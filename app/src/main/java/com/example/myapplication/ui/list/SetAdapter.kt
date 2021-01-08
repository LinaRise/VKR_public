package com.example.myapplication.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Set


class SetAdapter internal constructor(context: Context?, private val sets: List<Set>) :
    RecyclerView.Adapter<SetAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.sets_list_item_veiw, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val state: Set = sets[position]
        holder.setTitle.text = state.name
        holder.languageInput.text = state.languageInput
        holder.languageOutput.text = state.languageOutput
        holder.wordsAmount.text = state.languageInput
    }

    override fun getItemCount(): Int {
        return sets.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val setTitle: TextView = view.findViewById(R.id.set_title)
        val languageInput: TextView = view.findViewById(R.id.language_input)
        val languageOutput: TextView = view.findViewById(R.id.language_output)
        val wordsAmount: TextView = view.findViewById(R.id.words_amount)

    }

}