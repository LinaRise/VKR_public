package com.example.myapplication.ui.setCreate

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Word
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_set_create.*


class SetCreateActivity : AppCompatActivity(), ISetCreateView {
    private lateinit var deletedWord: Word
    private lateinit var recyclerView: RecyclerView
    private lateinit var setCreateAdapter: SetCreateAdapter
    private lateinit var wordAddButton: Button
    private lateinit var originalText: TextInputEditText
    private lateinit var translatedText: TextInputEditText
    lateinit var presenter: SetCreatePresenter

    // которые отображаются на экране
    var wordsDisplayed = ArrayList<Word>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        presenter = SetCreatePresenter(this)

        setContentView(R.layout.activity_set_create)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById(R.id.recyclerivew_set_create)
        wordAddButton = findViewById(R.id.word_add_button)
        originalText = findViewById(R.id.original_input)
        translatedText = findViewById(R.id.translated_input)
        recyclerView.layoutManager = LinearLayoutManager(this)
        getWordList()
        setCreateAdapter = SetCreateAdapter(this, wordsDisplayed)
        recyclerView.adapter = setCreateAdapter

        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        word_add_button.setOnClickListener { onAddWordBtnClick() }

    }

    private fun onAddWordBtnClick() {
        val original: String = originalText.text.toString().trim()
        val translated: String = translatedText.text.toString().trim()
        presenter.addNewWord(original, translated)
    }

    private fun getWordList() {
        //вызов загрузки
        presenter.loadData()
    }

    private var simpleCallBack =
        object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
            ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        ) {
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
                        presenter.deleteWord(deletedWord)

                    }
                }
            }

        }

    override fun setData(words: List<Word>) {
        wordsDisplayed.addAll(words)
    }

    override fun updateRecyclerViewInserted(word: Word) {
        wordsDisplayed.add(word)
        setCreateAdapter.notifyDataSetChanged()
    }

    override fun updateRecyclerViewDeleted(position: Int) {
        wordsDisplayed.removeAt(position)
        setCreateAdapter.notifyItemRemoved(position)

    }

    override fun showWordInputError() {
        Snackbar.make(
            recyclerView,
            getString(R.string.fill_in_both_lines),
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun showUndoDeleteWord(position: Int) {
        Snackbar.make(
            recyclerView,
            "${deletedWord.original} is deleted",
            Snackbar.LENGTH_LONG
        ).setAction("UNDO"
        ) {
            wordsDisplayed.add(position, deletedWord)
            setCreateAdapter.notifyItemInserted(position)
        }.show()
    }

    override fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            word_add_button.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    override fun cleanInputFields() {
        originalText.setText("")
        translatedText.setText("")


    }


}