package com.example.myapplication.ui.setCreate

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Word
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_set_create.*


class SetCreateActivity : AppCompatActivity(), ISetCreateView, ISetInputData {
    private lateinit var deletedWord: Word
    private lateinit var recyclerView: RecyclerView
    private lateinit var setCreateAdapter: SetCreateAdapter
    private lateinit var wordAddButton: Button
    private lateinit var originalText: TextInputEditText
    private lateinit var translatedText: InstantAutoComplete
    lateinit var presenter: SetCreatePresenter
    lateinit var dbhelper: DBHelper
    lateinit var db: SQLiteDatabase

    private lateinit var setTitle: String
    private lateinit var inputLanguage: String
    private lateinit var outputLanguage: String

    private var editTextTitle: EditText? = null
    private var hasAutoSuggest = 0
    private var editTextInputLang: AutoCompleteTextView? = null
    private var editTextOutputLang: AutoCompleteTextView? = null

    private var receivedTranslation: String = ""

    val languagesAccordance = hashMapOf(
        "English" to TranslateLanguage.ENGLISH,
        "Russian" to TranslateLanguage.RUSSIAN,
        "French" to TranslateLanguage.FRENCH,
        "Czech" to TranslateLanguage.CZECH,
        "German" to TranslateLanguage.GERMAN
    )


    // которые отображаются на экране
    var wordsDisplayed = ArrayList<Word>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        dbhelper = DBHelper(this)
        presenter = SetCreatePresenter(this, dbhelper)
        setContentView(R.layout.activity_set_create)

        val extras = intent.extras
        if (extras != null) {
            setTitle = extras.getString("setTitle").toString()
            inputLanguage = extras.getString("inputLanguage").toString()
            outputLanguage = extras.getString("outputLanguage").toString()
            hasAutoSuggest = extras.getInt("hasAutoSuggest")
        }

//        db = dbhelper.writableDatabase
        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerivew_set_create)
        wordAddButton = findViewById(R.id.word_add_button)
        originalText = findViewById(R.id.original_input)
        translatedText = findViewById(R.id.translated_input)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setCreateAdapter = SetCreateAdapter(this)
        recyclerView.adapter = setCreateAdapter

        setCreateAdapter.setData(wordsDisplayed)
//        getWordList()
        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        wordAddButton.setOnClickListener { onAddWordBtnClick() }
        originalText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                if (hasAutoSuggest == 1) {
                    val options = TranslatorOptions.Builder()
                        .setSourceLanguage(languagesAccordance[inputLanguage].toString())
                        .setTargetLanguage(languagesAccordance[outputLanguage].toString())
                        .build()
                    val translator = Translation.getClient(options)

                    translator.downloadModelIfNeeded()
                        .addOnSuccessListener {
                            // Model downloaded successfully. Okay to start translating.
                            // (Set a flag, unhide the translation UI, etc.)
                            translator.translate(s.toString())
                                .addOnSuccessListener { translatedTexts ->
                                    Toast.makeText(
                                        this@SetCreateActivity,
                                        translatedTexts,
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    receivedTranslation = translatedTexts
                                    val array = arrayOf(
                                        receivedTranslation
                                    )
                                    Toast.makeText(
                                        this@SetCreateActivity,
                                        "HERE $receivedTranslation",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()

                                    val adapter =
                                        ArrayAdapter(
                                            this@SetCreateActivity,
                                            android.R.layout.simple_list_item_1,
                                            array
                                        )
                                    translatedText.setAdapter(adapter)


                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(
                                        this@SetCreateActivity,
                                        "Translating problems: $exception",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    // Error.
                                    // ...
                                }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this@SetCreateActivity,
                                "Translating problems: $exception",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            // Model couldn’t be downloaded or other internal error.
                            // ...
                        }
                }
            }
        })

  /*      translatedText.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val array = arrayOf(
                    receivedTranslation
                )
                Toast.makeText(
                    this@SetCreateActivity,
                    "HERE $receivedTranslation",
                    Toast.LENGTH_LONG
                )
                    .show()

                val adapter =
                    ArrayAdapter(
                        this@SetCreateActivity,
                        android.R.layout.simple_list_item_1,
                        array
                    )
//                val adapter = ArrayAdapter.createFromResource(
//                    this@SetCreateActivity,
//                    R.array.available_translation_languages,
//                    android.R.layout.simple_list_item_1
//                )
                translatedText.setAdapter(adapter)
                translatedText.showDropDown()
            }
        })*/


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_new_activity_bar, menu)
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
                presenter.onDoneButtonWasClicked(
                    wordsDisplayed,
                    setTitle,
                    inputLanguage,
                    outputLanguage,
                    hasAutoSuggest
                )
                Toast.makeText(this, "added successfully", Toast.LENGTH_SHORT).show()
                finish()
                return true
            }
            R.id.ic_settings -> {
                val setCorrectInfoDialog = SetCorrectInfoDialog()
                val args = Bundle()
                args.putString("settTitle", setTitle)
                args.putString("inputLanguage", inputLanguage)
                args.putString("outputLanguage", outputLanguage)
                args.putInt("hasAutoSuggest", hasAutoSuggest)
                setCorrectInfoDialog.arguments = args
                val manager = supportFragmentManager
                setCorrectInfoDialog.show(manager, "Set Up Dialog")
                return true
            }
            else -> false
        }
    }


//    private fun onDoneButtonWasClicked() {
//        presenter.addAllToDatabase(wordsDisplayed, setTitle, inputLanguage, outputLanguage, db )
//        Toast.makeText(this, "added successfully", Toast.LENGTH_SHORT).show()
//        finish()
//    }


    private fun onAddWordBtnClick() {
        val original: String = originalText.text.toString().trim()
        val translated: String = translatedText.text.toString().trim()
        presenter.addNewWord(original, translated)
    }

    private fun getWordList() {
//        вызов загрузки
//        presenter.loadData()
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
                        presenter.deleteWord(deletedWord, position)

                    }
                }
            }

        }


//    override fun onDestroy() {
//        super.onDestroy()
//        dbhelper.close()
//
//    }

//    override fun setData(words: List<Word>) {
//        wordsDisplayed.addAll(words)
//    }

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
            "${deletedWord.originalWord} is deleted",
            Snackbar.LENGTH_LONG
        ).setAction(
            "UNDO"
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

    override fun onInputedData(list: ArrayList<Any>) {
        setTitle = (list[0] as String).trim()
        inputLanguage = (list[1] as String).trim()
        outputLanguage = (list[2] as String).trim()
        hasAutoSuggest = list[3] as Int

    }


}