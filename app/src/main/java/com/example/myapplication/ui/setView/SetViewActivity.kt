package com.example.myapplication.ui.setView

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettGetAsyncTask
import com.example.myapplication.database.repo.word.WordsGetAsyncTask
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.setCreate.ISetInputData
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_set_create.*


class SetViewActivity : AppCompatActivity(), ISetViewView, SettGetAsyncTask.TaskListener,
    ISetInputData {

    lateinit var presenter: SetViewPresenter
    lateinit var dbhelper: DBHelper
    private lateinit var recyclerView: RecyclerView
    private var settId: Long = -1

    private lateinit var wordAddButton: Button
    private lateinit var originalText: TextInputEditText
    private lateinit var translatedText: TextInputEditText

    private lateinit var setViewAdapter: SetViewAdapter
    private var openedSett: Sett? = null
    private  var inputLanguage: Language = Language()
    private  var outputLanguage: Language = Language()
    var wordsDisplayed = ArrayList<Word?>()
    var wordsEdited = ArrayList<Word?>()
    var wordsOriginal = ArrayList<Word>()
    private lateinit var deletedWord: Word

    private var setTitle: String? = ""
    private var inputLanguageText: String? = ""
    private var outputLanguageText: String? = ""
    private var editTextTitle: EditText? = null
    private var hasAutoSuggest = 0


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
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver,
             IntentFilter ("sending-list")
        )
        Log.d("SetViewActivity", "onCreate")
        wordAddButton = findViewById(R.id.word_add_button)
        wordAddButton.setOnClickListener { onAddWordBtnClick() }
    }

    var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
//            wordsOriginal = intent.getParcelableArrayListExtra("data")
            wordsDisplayed =  intent.getParcelableArrayListExtra("data")
            wordsEdited = ArrayList(wordsDisplayed)
            for (word in wordsDisplayed)
                if (word != null) {
                    wordsOriginal.add(Word(word.wordId,word.originalWord,word.translatedWord))
//                    wordsEdited.add(Word(word.wordId,word.originalWord,word.translatedWord))
                    //            wordsOriginal = ArrayList(intent.getParcelableArrayListExtra("data"))
                    Log.d("wordsOriginal", wordsOriginal.size.toString())

                }
            Log.d("wordsOriginal", wordsOriginal.size.toString())
            Toast.makeText(this@SetViewActivity,"words received ${wordsOriginal.size.toString()}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        SettGetAsyncTask(dbhelper, this).execute(settId)
        recyclerView = findViewById(R.id.recyclerivew_set_create)
        originalText = findViewById(R.id.original_input)
        translatedText = findViewById(R.id.translated_input)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setViewAdapter = SetViewAdapter(this)
        recyclerView.adapter = setViewAdapter
        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun onAddWordBtnClick() {
        val original: String = originalText.text.toString().trim()
        val translated: String = translatedText.text.toString().trim()
        presenter.addNewWord(original, translated)
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
            R.id.home -> {
                finish()
                return true
            }
            R.id.check_icon -> {
//               Log.d("wordsDisplayed", wordsDisplayed[0].toString())
//               Log.d("wordsOriginal", wordsOriginal[0].toString())
//               Log.d("wordsEdited", wordsEdited[0].toString())
//                wordsDisplayed.forEach { print(it) }
//                wordsOriginal.forEach { print(it) }
//                wordsEdited.forEach { print(it) }
                Toast.makeText(this, "wordsEdited size = ${wordsEdited.size}", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "wordsOrgigna size = ${wordsOriginal[0]}}", Toast.LENGTH_SHORT).show()
                presenter.onDoneButtonWasClicked(
                    wordsDisplayed,
                    wordsOriginal,
                    wordsEdited,
                    openedSett,
                    inputLanguage.languageTitle,
                    outputLanguage.languageTitle,
                    hasAutoSuggest
                )

//                Toast.makeText(this, "added successfully", Toast.LENGTH_SHORT).show()
                finish()
                return true
            }
            R.id.ic_settings -> {
                Toast.makeText(this, outputLanguageText, Toast.LENGTH_SHORT).show()
//                val setCorrectInfoDialog = SetCorrectInfoDialog()
//                val args = Bundle()
//                args.putString("settTitle", setTitle)
//                args.putString("inputLanguage", inputLanguageText)
//                args.putString("outputLanguage", outputLanguageText)
//                args.putInt("hasAutoSuggest", hasAutoSuggest)
//                setCorrectInfoDialog.arguments = args
//                val manager = supportFragmentManager
//                setCorrectInfoDialog.show(manager, "Set Up Dialog")
                return true
            }

            else -> false
        }
    }


    override fun onSettReceived(sett: Sett?) {
        Log.d("SetViewActivity", "onSettReceived")
        if (sett != null) {
            supportActionBar?.title = sett.settTitle
            setTitle = sett.settTitle
            openedSett = sett
            inputLanguage = LanguageRepo(dbhelper).get(sett.languageInput_id)!!
            outputLanguage = LanguageRepo(dbhelper).get(sett.languageOutput_id)!!
            inputLanguageText = inputLanguage?.languageTitle
            outputLanguageText = outputLanguage?.languageTitle
            hasAutoSuggest = sett.hasAutoSuggest
            WordsGetAsyncTask(dbhelper, setViewAdapter).execute(sett.settId)

        } else {
            supportActionBar?.title = "Set"
        }
    }

//    override fun setData(words: List<Word>) {
//        wordsDisplayed.addAll(words)
//        wordsEdited.addAll(wordsDisplayed)
//    }

    override fun updateRecyclerViewInserted(word: Word) {
        wordsDisplayed.add(word)
        wordsEdited.add(word)
        Toast.makeText(this, "wordsEdited size = ${wordsEdited.size}", Toast.LENGTH_SHORT).show()
        setViewAdapter.notifyItemInserted(wordsDisplayed.size-1)


    }


    override fun updateRecyclerViewDeleted(position: Int) {
        wordsEdited[position] = null
        setViewAdapter.notifyItemRemoved(position)
        wordsDisplayed.removeAt(position)

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
            "${deletedWord.originalWord} is deleted",
            Snackbar.LENGTH_LONG
        ).setAction(
            "UNDO"
        ) {
            wordsDisplayed.add(position, deletedWord)
            setViewAdapter.notifyItemInserted(position)
            wordsEdited.add(position, deletedWord)
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

    override fun onInputedData(list: ArrayList<Any>) {
        setTitle = (list[0] as String).trim()
        inputLanguageText = (list[1] as String).trim()
        outputLanguageText = (list[2] as String).trim()
        hasAutoSuggest = list[3] as Int
        openedSett?.settTitle = setTitle as String
        openedSett?.hasAutoSuggest = hasAutoSuggest
        inputLanguage.languageTitle = inputLanguageText as String
        outputLanguage.languageTitle = outputLanguageText as String
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
                        deletedWord = wordsDisplayed[position]!!
                        presenter.deleteWord(deletedWord,position)

                    }
                }
            }

        }


}