package com.example.myapplication.ui.setView

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.sett.SettGetAsyncTask
import com.example.myapplication.database.repo.word.WordsGetAsyncTask
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.setCreate.SetCreateAdapter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_set_create.*

class SetViewActivity : AppCompatActivity(), ISetViewView, SettGetAsyncTask.TaskListener {

    lateinit var presenter: SetViewPresenter
    lateinit var dbhelper: DBHelper
    private lateinit var recyclerView: RecyclerView
    private var settId: Long = -1

    private lateinit var wordAddButton: Button
    private lateinit var originalText: TextInputEditText
    private lateinit var translatedText: TextInputEditText

    private lateinit var setViewAdapter: SetViewAdapter

    var wordsDisplayed = ArrayList<Word>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbhelper = DBHelper(this)
        presenter = SetViewPresenter(this, dbhelper)
        val extras = intent.extras
        if (extras != null) {
            settId = extras.getLong("settId")
        }

        Log.d("SetViewActivity", "onCreate")

    }

    override fun onStart() {
        super.onStart()
        SettGetAsyncTask(dbhelper, this).execute(settId)
        recyclerView = findViewById(R.id.recyclerivew_set_create)
        originalText = findViewById(R.id.original_input)
        translatedText = findViewById(R.id.translated_input)
        recyclerView.layoutManager = LinearLayoutManager(this)
        wordAddButton = findViewById(R.id.word_add_button)
        setViewAdapter = SetViewAdapter(this)
        recyclerView.adapter = setViewAdapter



        word_add_button.setOnClickListener { onAddWordBtnClick() }
    }

    private fun onAddWordBtnClick() {
      /*  val original: String = originalText.text.toString().trim()
        val translated: String = translatedText.text.toString().trim()
        presenter.addNewWord(original, translated)*/
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.set_view_bar, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
//            R.id.home -> {
//                onBackPressed()
//                Log.d("SetViewActivity", "onBackPress")
//                return true
//            }

            else -> false
        }
    }




    override fun onSettReceived(sett: Sett?) {
        Log.d("SetViewActivity", "onSettReceived")
        if (sett != null) {
            supportActionBar?.title = sett.settTitle
            WordsGetAsyncTask(dbhelper,setViewAdapter).execute(sett.settId)

        } else {
            supportActionBar?.title = "Set"
        }
    }

    override fun setData(words: List<Word>) {
        TODO("Not yet implemented")
    }

    override fun updateRecyclerViewInserted(word: Word) {
        TODO("Not yet implemented")
    }

    override fun updateRecyclerViewDeleted(position: Int) {
        TODO("Not yet implemented")
    }

    override fun showWordInputError() {
        TODO("Not yet implemented")
    }

    override fun showUndoDeleteWord(position: Int) {
        TODO("Not yet implemented")
    }

    override fun hideKeyboard() {
        TODO("Not yet implemented")
    }

    override fun cleanInputFields() {
        TODO("Not yet implemented")
    }


}