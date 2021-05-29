package com.example.myapplication.ui.setView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.connectivity.base.ConnectivityProvider
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.DependencyInjectorImpl
import com.example.myapplication.ui.setCreate.ISetInputData
import com.example.myapplication.ui.setCreate.InstantAutoComplete
import com.example.myapplication.ui.setCreate.correctInfoDialog.SetCorrectInfoDialog
import com.example.myapplication.ui.setView.copyCardDialog.CopyCardDialog
import com.example.myapplication.ui.study.StudyActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import kotlinx.android.synthetic.main.activity_set_create.*
import java.io.IOException


class SetViewActivity : AppCompatActivity(), SetViewContract.View,
    ISetInputData, ConnectivityProvider.ConnectivityStateListener {

    private lateinit var presenter: SetViewContract.Presenter
    lateinit var dbhelper: DBHelper
    private lateinit var setViewAdapter: SetViewAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: ArrayAdapter<Any>

    /**
     * переменная для отслеживания подключения к интеренету
     */
    private val provider: ConnectivityProvider by lazy { ConnectivityProvider.createProvider(this) }
    private var hasInternet: Boolean = false

    private lateinit var wordAddButton: Button
    private lateinit var originalText: TextInputEditText
    private lateinit var translatedText: InstantAutoComplete

    private var openedSett: Sett? = null
    private var inputLanguage: Language = Language()
    private var outputLanguage: Language = Language()


    var wordsDisplayed = ArrayList<Word?>()
    var wordsOriginal = ArrayList<Word>()
    private lateinit var deletedWord: Word

    private var settId: Long = -1
    private var receivedCopiedText: String = ""
    private var setTitle: String? = ""
    private var inputLanguageText: String? = ""
    private var outputLanguageText: String? = ""
    private var hasAutoSuggest = 0

    var translate: Translate? = null
    var languageTitleAndCode: Map<String, String> = hashMapOf()
    private var receivedTranslation: String = ""


    /**
     * Инициализация сервиса перевода
     */
    private val translateService: Unit
        get() {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            try {

                resources.openRawResource(R.raw.credentials).use { `is` ->
                    //получаем реквизиты для входа
                    val myCredentials = GoogleCredentials.fromStream(`is`)
                    //получаем сервис перевода:
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
        Log.d("SetViewActivity", "onCreate")
        setContentView(R.layout.activity_set_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbhelper = DBHelper(this)
        setPresenter(SetViewPresenter(this, DependencyInjectorImpl(dbhelper)))

        //получаем скопированный текст, если он есть, и id набора слов
        val extras = intent.extras
        if (extras != null) {
            if (intent.getStringExtra("copiedText") != null) {
                receivedCopiedText = extras.getString("copiedText").toString()
            }
            settId = extras.getLong("settId")
        }


        /*LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver,
            IntentFilter("sending-list")
        )*/

        //инициализация элементов
        wordAddButton = findViewById(R.id.word_add_button)
        wordAddButton.setOnClickListener { onAddWordBtnClick() }
        recyclerView = findViewById(R.id.recyclerivew_set_create)
        originalText = findViewById(R.id.original_input)
        translatedText = findViewById(R.id.translated_input)
        originalText.setText(receivedCopiedText)

    }

    /*var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
            wordsDisplayed = intent.getParcelableArrayListExtra("data")
            for (word in wordsDisplayed)
                if (word != null) {
                    wordsOriginal.add(
                        Word(
                            word.wordId,
                            word.originalWord,
                            word.translatedWord,
                            settId = word.settId,
                            recallPoint = word.recallPoint
                        )
                    )
                    Log.d("wordsOriginal", wordsOriginal.size.toString())
                }
            Log.d("wordsOriginal", wordsOriginal.size.toString())
        }


    }*/

    override fun onStart() {
        super.onStart()
        Log.d("SetViewActivity", "onStart")
        provider.addListener(this)

        presenter.onViewCreated(settId)
//        SettGetAsyncTask(dbhelper, this).execute(settId)

        recyclerView.layoutManager = LinearLayoutManager(this)
        setViewAdapter = SetViewAdapter()
        recyclerView.adapter = setViewAdapter

        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        adapter =
            ArrayAdapter(
                this@SetViewActivity,
                android.R.layout.simple_list_item_1,
                emptyArray()
            )

        translatedText.onFocusChangeListener = View.OnFocusChangeListener { p0, p1 ->
            if (p1) {
                if (translate != null) {
                    receivedTranslation = presenter.onTranslate(
                        translate!!,
                        languageTitleAndCode,
                        originalText.text.toString(),
                        inputLanguageText!!.trim(),
                        outputLanguageText!!.trim(),
                        hasAutoSuggest,
                        hasInternet
                    )
                } else {
                    showNoTranslationServiceAvailable()
                }
                Log.d("receivedTranslation", receivedTranslation)

                val array = arrayOf(
                    receivedTranslation
                )
                adapter =
                    ArrayAdapter(
                        this@SetViewActivity,
                        android.R.layout.simple_list_item_1,
                        array
                    )
                adapter.notifyDataSetChanged()
                translatedText.setAdapter(adapter)
                translatedText.showDropDown()

            }
        }
    }

    override fun showNoTranslationServiceAvailable() {
        Toast.makeText(this, "Problems with translation server initialization", Toast.LENGTH_SHORT)
            .show()
    }


    private fun onAddWordBtnClick() {
        val original: String = originalText.text.toString().trim()
        val translated: String = translatedText.text.toString().trim()
        adapter.clear()
        adapter.notifyDataSetChanged()
        presenter.onAddWordClicked(original, translated)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        dbhelper.close()

    }

    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
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
        return when (item.itemId) {
            R.id.home -> {
                Log.d("SetViewActivity", "home clicked")

                finish()
                return true
            }
            R.id.check_icon -> {
                Log.d("SetViewActivity", "check_icon clicked")

                presenter.onSaveClicked(
                    wordsDisplayed,
                    wordsOriginal,
                    openedSett,
                    inputLanguage.languageTitle,
                    outputLanguage.languageTitle,
                    hasAutoSuggest
                )

                finish()
                return true
            }
            R.id.ic_settings -> {
                Log.d("SetViewActivity", "ic_settings clicked")
                showSetCorrectInfoDialog()
                return true
            }
            R.id.ic_study -> {
                Log.d("SetViewActivity", "ic_study clicked")

                if (wordsDisplayed.size < 5) {
                    Snackbar.make(
                        recyclerView,
                        "You need at least 5 words to start studying",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    val intent = Intent(this, StudyActivity::class.java)
                    intent.putExtra("wordsDisplayed", wordsDisplayed)
                    startActivity(intent)
                }
                return true

            }

            else -> false
        }
    }


    /* override fun onSettReceived(sett: Sett?) {
         Log.d("SetViewActivity", "onSettReceived")
         if (sett != null) {
             supportActionBar?.title = sett.settTitle
             setTitle = sett.settTitle
             openedSett = sett
             inputLanguage = LanguageRepo(dbhelper).get(sett.languageInput_id)!!
             outputLanguage = LanguageRepo(dbhelper).get(sett.languageOutput_id)!!
             inputLanguageText = inputLanguage.languageTitle
             outputLanguageText = outputLanguage.languageTitle
             hasAutoSuggest = sett.hasAutoSuggest
             presenter.getSetWords(sett.settId)
 //            WordsGetAsyncTask(dbhelper, setViewAdapter).execute(sett.settId)
         } else {
             supportActionBar?.title = "Set"
         }
     }*/


    private fun showSetCorrectInfoDialog() {
        val setCorrectInfoDialog = SetCorrectInfoDialog()
        val args = Bundle()
        args.putString("settTitle", setTitle)
        args.putString("inputLanguage", inputLanguageText)
        args.putString("outputLanguage", outputLanguageText)
        args.putInt("hasAutoSuggest", hasAutoSuggest)
        setCorrectInfoDialog.arguments = args
        val manager = supportFragmentManager
        setCorrectInfoDialog.show(manager, "Set Up Dialog")
    }


    override fun updateRecyclerViewInserted(word: Word) {
        wordsDisplayed.add(word)
        setViewAdapter.notifyItemInserted(wordsDisplayed.size - 1)


    }


    override fun updateRecyclerViewDeleted(position: Int) {
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

    /**
     * показ кнопки отмены удаления слова
     */
    override fun showUndoDeleteWord(position: Int) {
        Snackbar.make(
            recyclerView,
            deletedWord.originalWord + " " + getText(R.string.is_deleted),
            Snackbar.LENGTH_LONG
        ).setAction(
            getString(R.string.undo)
        ) {
            wordsDisplayed.add(position, deletedWord)
            setViewAdapter.notifyItemInserted(position)
        }.show()
    }

    /**
     * функция скрытия клавиатуры
     */
    override fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            word_add_button.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    /**
     * функция очистки полей
     */
    override fun cleanInputFields() {
        originalText.setText("")
        translatedText.setText("")
    }

    override fun setData(result: List<Word>?) {
        setViewAdapter.setData(result)
        setViewAdapter.notifyDataSetChanged()
        wordsDisplayed = result as ArrayList<Word?>
        for (word in wordsDisplayed)
            if (word != null) {
                wordsOriginal.add(
                    Word(
                        word.wordId,
                        word.originalWord,
                        word.translatedWord,
                        settId = word.settId,
                        recallPoint = word.recallPoint
                    )
                )
                Log.d("wordsOriginal", wordsOriginal.size.toString())
            }
        Log.d("wordsOriginal", wordsOriginal.size.toString())


    }


    override fun showCheckInternetConnection() {
        Toast.makeText(
            this@SetViewActivity,
            "Can't load available languages for translation! Check internet connection!",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun setPresenter(presenter: SetViewContract.Presenter) {
        this.presenter = presenter
    }


    /**
     * функция присваивания данных
     */
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

    /**
     * функция подключения жестов для карточек
     */
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

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    //свайп влево -> удаление
                    ItemTouchHelper.LEFT -> {
                        deletedWord = wordsDisplayed[position]!!
                        presenter.onLeftSwipe(position)
                    }
                    //свайп впрао -> копирование
                    ItemTouchHelper.RIGHT -> {
                        setViewAdapter.notifyItemChanged(viewHolder.adapterPosition)
                        val sets = presenter.onRightSwipe()
                        showDialog(sets, position)

                    }
                }
            }

        }

    /**
     * вызов диалога для копирования слова
     */
    override fun showDialog(sets: List<Sett>?, position: Int) {
        val copyCardDialog =
            CopyCardDialog(sets!!, wordsDisplayed[position], openedSett!!)
        copyCardDialog.show(supportFragmentManager, "Copy card dialog")
    }

    override fun setSettData(resultSett: Sett) {
        supportActionBar?.title = resultSett.settTitle
        setTitle = resultSett.settTitle
        openedSett = resultSett
        hasAutoSuggest = resultSett.hasAutoSuggest
        presenter.loadLanguagesData(resultSett)
    }

    override fun setLanguageData(inputLang: Language, outputLang: Language) {
        inputLanguage = inputLang
        outputLanguage = outputLang
        inputLanguageText = inputLanguage.languageTitle
        outputLanguageText = outputLanguage.languageTitle
    }


    /**
     * функция проверки состояния подключения к интернету
     */
    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        hasInternet = state.hasInternet()
        if (hasInternet) {
            translateService
            if (translate != null) {
                if (languageTitleAndCode.isEmpty()) {
                    val languages: List<com.google.cloud.translate.Language> =
                        translate!!.listSupportedLanguages()
                    languageTitleAndCode = languages.map { it.name to it.code }.toMap()
                }
            }
        } else {
            showCheckInternetConnection()

        }
    }


    private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }


}