package com.example.myapplication.ui.setCreate

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.connectivity.base.ConnectivityProvider
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Word
import com.example.myapplication.translation.TranslationUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Language
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import kotlinx.android.synthetic.main.activity_set_create.*
import java.io.IOException


class SetCreateActivity : AppCompatActivity(), ISetCreateView, ISetInputData,
    ConnectivityProvider.ConnectivityStateListener {
    private var connected = false
    var translate: Translate? = null

    private val provider: ConnectivityProvider by lazy { ConnectivityProvider.createProvider(this) }

    private var hasInternet: Boolean = false;

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

    lateinit var adapter: ArrayAdapter<Any>


    private val translateService: Unit
        get() {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            try {

                resources.openRawResource(R.raw.credentials).use { `is` ->

                    //Get credentials:
                    val myCredentials = GoogleCredentials.fromStream(`is`)

                    //Set credentials and get translate service:
                    val translateOptions =
                        TranslateOptions.newBuilder().setCredentials(myCredentials).build()
                    translate = translateOptions.service
                }
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }


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
        var languageTitleAndCode: Map<String, String> = hashMapOf()
        translateService
        if (translate != null) {
            val languages: List<Language> = translate!!.listSupportedLanguages()
            languageTitleAndCode = languages.map { it.name to it.code }.toMap()
        }

        wordAddButton.setOnClickListener { onAddWordBtnClick() }
        /* translatedText.addTextChangedListener(object : TextWatcher {
             override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

             }

             override fun beforeTextChanged(
                 s: CharSequence, start: Int, count: Int,
                 after: Int
             ) {
                 Toast.makeText(
                     this@SetCreateActivity,
                     "hasAutoSuggest $hasAutoSuggest",
                     Toast.LENGTH_LONG
                 )
                     .show()


             }


             override fun afterTextChanged(s: Editable) {

             }

         })*/

        /* originalText.onFocusChangeListener = View.OnFocusChangeListener { p0, p1 ->
             if (p1) {
                 receivedTranslation = ""
             }
         }*/
        translatedText.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p1) {
                    if (hasAutoSuggest == 1) {
                        if (hasInternet) {

                            receivedTranslation =
                                TranslationUtils.translate(
                                    translate!!,
                                    languageTitleAndCode,
                                    originalText.text.toString(),
                                    inputLanguage.trim(),
                                    outputLanguage.trim()
                                )
                            Log.d("receivedTranslation", receivedTranslation)

                            val array = arrayOf(
                                receivedTranslation
                            )

                            Toast.makeText(
                                this@SetCreateActivity,
                                "HERE $inputLanguage, $outputLanguage",
                                Toast.LENGTH_LONG
                            ).show()

                            adapter =
                                ArrayAdapter(
                                    this@SetCreateActivity,
                                    android.R.layout.simple_list_item_1,
                                    array
                                )
                            adapter.notifyDataSetChanged()

                            translatedText.setAdapter(adapter)
                            translatedText.showDropDown();

                        }
                        Toast.makeText(
                            this@SetCreateActivity,
                            "HERE 2 $receivedTranslation",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
//                } else if (!p1) {
//                    adapter.clear()
//                    adapter.notifyDataSetChanged()
//                }

            }


        }
    }


    override fun onStart() {
        super.onStart()
        provider.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
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
        adapter.clear()
        adapter.notifyDataSetChanged()
        presenter.addNewWord(original, translated)
    }

    private fun getWordList() {
//        вызов загрузки
//        presenter.loadData()
    }

    private
    var simpleCallBack =
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

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
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
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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

    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        hasInternet = state.hasInternet()
    }

    private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }

}