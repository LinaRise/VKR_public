package com.example.myapplication.ui.setAdd

import android.content.ClipData
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Word
import com.google.android.material.snackbar.Snackbar

class SetCreateActivity : AppCompatActivity() {
    private lateinit var deletedWord: Word
    lateinit var recyclerView: RecyclerView
    lateinit var setCreateAdapter: SetCreateAdapter
    // которые отображаются на экране
    var wordsDisplayed =ArrayList<Word>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_create)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById(R.id.recyclerivew_set_create)
        recyclerView.layoutManager = LinearLayoutManager(this)
        setCreateAdapter = SetCreateAdapter(this, getWordList())
        recyclerView.adapter = setCreateAdapter

        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun getWordList(): ArrayList<Word> {
        var words = ArrayList<Word>()
        var word1 = Word("111111111111111111111", "1")
        var word2 = Word("2", "2")
        var word3 = Word("3111111111111", "3")
        var word4 = Word("4111111111", "4")
        var word5 = Word("5", "6")
        words.add(word1)
        words.add(word2)
        words.add(word3)
        words.add(word4)
        words.add(word5)
        wordsDisplayed.addAll(words)
        return wordsDisplayed
    }

    private var simpleCallBack =
        object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        deletedWord = wordsDisplayed[position]
                        wordsDisplayed.removeAt(position)
                        setCreateAdapter.notifyItemRemoved(position)

                        Snackbar.make(
                            recyclerView,
                            "${deletedWord.original} is deleted",
                            Snackbar.LENGTH_LONG
                        ).setAction("UNDO",
                            View.OnClickListener {
                                wordsDisplayed.add(position, deletedWord)
                                setCreateAdapter.notifyItemInserted(position)
                            }).show()
                    }
                }
            }

        }


}