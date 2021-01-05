package com.example.myapplication.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Set


class SetAdapter internal constructor(context: Context?, private val states: List<Set>) :
    RecyclerView.Adapter<SetAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.sets_list_item_veiw, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val state: Set = states[position]
        holder.nameView.text = state.name
        holder.capitalView.text = state.capital
    }

    override fun getItemCount(): Int {
        return states.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.name)
        val capitalView: TextView = view.findViewById(R.id.capital)

    }

}