package com.example.myapplication.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Sett


// класс адаптер для отображения наборов слов через RecyclerView
class SetAdapter internal constructor(
    context: Context?, onSetListener: OnSetListener,
    private val sets: LinkedHashMap<Sett, List<String>>
) :
    RecyclerView.Adapter<SetAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var onSetListener: OnSetListener = onSetListener

    //передаем шаблон строки
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.sets_list_item_veiw, parent, false)
        return ViewHolder(view, onSetListener)
    }

    // передаем информацию для отображения
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val state: Sett = sets[position]
        val listValues: List<List<String>> = ArrayList<List<String>>(sets.values)
        val listKeys: List<Sett> = ArrayList<Sett>(sets.keys)
        val langList: List<*> = listValues[position]
        holder.setTitle.text = listKeys[position].settTitle
        holder.languageInput.text = langList[0].toString()
        holder.languageOutput.text = langList[1].toString()
        holder.wordsAmount.text = listKeys[position].wordsAmount.toString()
    }

    // возврат размера
    override fun getItemCount(): Int {
        return sets.size
    }

    class ViewHolder(view: View, onNoteListener: OnSetListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        val setTitle: TextView = view.findViewById(R.id.set_title)
        val languageInput: TextView = view.findViewById(R.id.language_input)
        val languageOutput: TextView = view.findViewById(R.id.language_output)
        val wordsAmount: TextView = view.findViewById(R.id.words_amount)
        var mOnSetListener: OnSetListener = onNoteListener
        override fun onClick(view: View) {
            mOnSetListener.onSetClicked(adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    interface OnSetListener {
        fun onSetClicked(position: Int)

    }

}