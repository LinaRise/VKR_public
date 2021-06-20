package com.example.myapplication.ui.setView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.setCreate.InstantAutoComplete
import com.google.android.material.textfield.TextInputEditText


class SetViewAdapter : RecyclerView.Adapter<SetViewAdapter.SetViewHolder>() {
   private lateinit var context: Context
    private var words = ArrayList<Word?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_set_create_word, parent, false)
        return SetViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
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
            this.words = word as ArrayList<Word?>
            notifyDataSetChanged()

       /* val intent = Intent("sending-list")
        val bundle = Bundle()
        bundle.putParcelableArrayList("data", words)
        intent.putExtras(bundle)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);*/
        }
    }





    inner class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var original: TextInputEditText = itemView.findViewById(R.id.original_input)
        var translated: InstantAutoComplete = itemView.findViewById(R.id.translated_input)

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