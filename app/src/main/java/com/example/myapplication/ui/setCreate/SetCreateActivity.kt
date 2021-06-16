package com.example.myapplication.ui.setCreate

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
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
import com.example.myapplication.ui.DependencyInjectorImpl
import com.example.myapplication.ui.setCreate.correctInfoDialog.SetCorrectInfoDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Language
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import kotlinx.android.synthetic.main.activity_set_create.*
import java.io.IOException


class SetCreateActivity : AppCompatActivity(), SetCreateContract.View, ISetInputData,
    ConnectivityProvider.ConnectivityStateListener {

    var translate: Translate? = null

    private val provider: ConnectivityProvider by lazy { ConnectivityProvider.createProvider(this) }
    private var hasInternet: Boolean = false;

    private lateinit var deletedWord: Word
    private lateinit var recyclerView: RecyclerView
    private lateinit var setCreateAdapter: SetCreateAdapter
    private lateinit var wordAddButton: Button
    private lateinit var originalText: TextInputEditText
    private lateinit var translatedText: InstantAutoComplete

    private lateinit var presenter: SetCreateContract.Presenter
    lateinit var dbhelper: DBHelper

    private lateinit var setTitle: String
    private lateinit var inputLanguage: String
    private lateinit var outputLanguage: String
    private var receivedTranslation: String = ""

    private var hasAutoSuggest = 0

    lateinit var adapter: ArrayAdapter<Any>

    var languageTitleAndCode: Map<String, String> = hashMapOf()

    // которые отображаются на экране
    var wordsDisplayed = ArrayList<Word>()

    private val translateService: Unit
        get() {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            try {
                resources.openRawResource(R.raw.credentials).use { `is` ->
                    val myCredentials = GoogleCredentials.fromStream(`is`)
                    val translateOptions =
                        TranslateOptions.newBuilder().setCredentials(myCredentials).build()
                    translate = translateOptions.service
                }
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbhelper = DBHelper(this)
        setPresenter(SetCreatePresenter(this, DependencyInjectorImpl(dbhelper)))

        setContentView(R.layout.activity_set_create)

        val extras = intent.extras
        if (extras != null) {
            setTitle = extras.getString("setTitle").toString()
            inputLanguage = extras.getString("inputLanguage").toString()
            outputLanguage = extras.getString("outputLanguage").toString()
            hasAutoSuggest = extras.getInt("hasAutoSuggest")
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerivew_set_create)
        wordAddButton = findViewById(R.id.word_add_button)
        originalText = findViewById(R.id.original_input)
        translatedText = findViewById(R.id.translated_input)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setCreateAdapter = SetCreateAdapter()
        recyclerView.adapter = setCreateAdapter
        setCreateAdapter.setData(wordsDisplayed)

        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        wordAddButton.setOnClickListener { onAddWordBtnClick() }

        adapter =
            ArrayAdapter(
                this@SetCreateActivity,
                android.R.layout.simple_list_item_1,
                emptyArray()
            )

        translatedText.onFocusChangeListener = View.OnFocusChangeListener { p0, p1 ->
            if (p1) {
                receivedTranslation = presenter.onTranslate(
                    translate!!,
                    languageTitleAndCode,
                    originalText.text.toString(),
                    inputLanguage.trim(),
                    outputLanguage.trim(),
                    hasAutoSuggest,
                    hasInternet
                )
                val array = arrayOf(
                    receivedTranslation
                )
                adapter =
                    ArrayAdapter(
                        this@SetCreateActivity,
                        android.R.layout.simple_list_item_1,
                        array
                    )
                adapter.notifyDataSetChanged()
                translatedText.setAdapter(adapter)
                translatedText.showDropDown();

                /*     if (hasAutoSuggest == 1) {
                         if (hasInternet) {
                             receivedTranslation =
                                 presenter.onTranslate(
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
                     }*/
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

    override fun setPresenter(presenter: SetCreateContract.Presenter) {
        this.presenter = presenter
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
        return when (item.itemId) {
            R.id.home -> {
                finish()
                return true
            }
            R.id.check_icon -> {
                presenter.onSaveClicked(
                    wordsDisplayed,
                    setTitle,
                    inputLanguage,
                    outputLanguage,
                    hasAutoSuggest
                )
                finish()
                return true
            }
            R.id.ic_settings -> {
                showSetCorrectInfoDialog()
                return true
            }
            else -> false
        }
    }


    private fun showSetCorrectInfoDialog() {
        val setCorrectInfoDialog = SetCorrectInfoDialog()
        val args = Bundle()
        args.putString("settTitle", setTitle)
        args.putString("inputLanguage", inputLanguage)
        args.putString("outputLanguage", outputLanguage)
        args.putInt("hasAutoSuggest", hasAutoSuggest)
        setCorrectInfoDialog.arguments = args
        val manager = supportFragmentManager
        setCorrectInfoDialog.show(manager, "Set Up Dialog")
    }

    private fun onAddWordBtnClick() {
        val original: String = originalText.text.toString().trim()
        val translated: String = translatedText.text.toString().trim()
        adapter.clear()
        adapter.notifyDataSetChanged()
        presenter.onAddWordClicked(original, translated)
    }


    private var simpleCallBack =
        object : ItemTouchHelper.SimpleCallback(
            0.or(0),
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
                        presenter.onLeftSwipe(position)
                    }
                }
            }

        }

    /**
     * обновление списка после вставки
     */
    override fun updateRecyclerViewInserted(word: Word) {
        wordsDisplayed.add(word)
        setCreateAdapter.notifyDataSetChanged()
    }

    /**
     * обновление списка после удаления
     */
    override fun updateRecyclerViewDeleted(position: Int) {
        wordsDisplayed.removeAt(position)
        setCreateAdapter.notifyItemRemoved(position)

    }

    /**
     * показ ошибки ввода
     */
    override fun showWordInputError() {
        Snackbar.make(
            recyclerView,
            R.string.fill_in_both_lines,
            Snackbar.LENGTH_LONG
        ).show()
    }

    /**
     * показать отмену удаления
     */
    override fun showUndoDeleteWord(position: Int) {
        Snackbar.make(
            recyclerView,
            deletedWord.originalWord +" "+ getText(R.string.is_deleted),
            Snackbar.LENGTH_LONG
        ).setAction(
            R.string.undo
        ) {
            wordsDisplayed.add(position, deletedWord)
            setCreateAdapter.notifyItemInserted(position)
        }.show()
    }

    /**
     * спрятать клавиатуру
     */
    override fun hideKeyboard() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            word_add_button.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    /**
     * очистка полей
     */
    override fun cleanInputFields() {
        originalText.setText("")
        translatedText.setText("")
    }

    /**
     * показать ссообщение
     */
    override fun showSuccessSavedToast() {
        Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show()
    }

    /**
     * спрятать клвиатуру
     */
    override fun onInputedData(list: ArrayList<Any>) {
        setTitle = (list[0] as String).trim()
        inputLanguage = (list[1] as String).trim()
        outputLanguage = (list[2] as String).trim()
        hasAutoSuggest = list[3] as Int
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        dbhelper.close()

    }

    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        hasInternet = state.hasInternet()
        if (hasInternet) {
            translateService
            if (translate != null) {
                if (languageTitleAndCode.isEmpty()) {
                    val languages: List<Language> =
                        translate!!.listSupportedLanguages()
                    languageTitleAndCode = languages.map { it.name to it.code }.toMap()
                }
            }
        } else {
            Toast.makeText(
                this,
                "Can't load available languages for translation! Check internet connection!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }

}