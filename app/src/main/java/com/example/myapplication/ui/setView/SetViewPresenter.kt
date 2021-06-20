package com.example.myapplication.ui.setView

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.translation.TranslationUtils
import com.example.myapplication.ui.DependencyInjector
import com.google.cloud.translate.Translate
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class SetViewPresenter(
    view: SetViewContract.View,
    dependencyInjector: DependencyInjector
) : SetViewContract.Presenter {
    private var mView: SetViewContract.View? = view
    private val mSettRepo: SettRepo = dependencyInjector.settRepository()
    private val mLanguageRepo: LanguageRepo = dependencyInjector.languageRepository()
    private val mWordRepo: WordRepo = dependencyInjector.wordRepository()


    /**
     * Добавление нового слова в список
     * @param original - текст для перевода
     * @param translated -переведенный тектс
     */
    override fun onAddWordClicked(original: String, translated: String) {
        if (original == "" || translated == "") {
            mView?.showWordInputError()
            return
        }
        mView?.hideKeyboard()
        val word = Word(0, original.trim(), translated.trim())
        mView?.cleanInputFields()
        mView?.updateRecyclerViewInserted(word)
    }

    /**
     * Перевод слова
     * @param translate - переменная сервиса перевода
     * @param languageCodeAndTitle - код и название языков
     * @param originalText - текст для перевода
     * @param sourceLanguage - язык с которого нужен перевод
     * @param targetLanguage - язык на который нужно перевести
     * @return String перевод
     */
    override fun onTranslate(
        translate: Translate,
        languageCodeAndTitle: Map<String, String>,
        originalText: String,
        sourceLanguage: String,
        targetLanguage: String,
        hasAutoSuggest: Int,
        hasInternet: Boolean
    ): String {
        val sourceLangCode = mLanguageRepo.getByLocaleTitle(sourceLanguage)!!.languageId
        val targetLangCode = mLanguageRepo.getByLocaleTitle(targetLanguage)!!.languageId
        if (hasAutoSuggest == 1) {
            if (hasInternet) {
                return TranslationUtils.translate(
                    translate,
                    languageCodeAndTitle,
                    originalText,
                    sourceLangCode,
                    targetLangCode
                )
            }
        }
        return ""

    }


    /**
     * Удаление слова из спсика
     * @param position - позиция слова в списке на экране
     */
    override fun onLeftSwipe(position: Int) {
        mView?.updateRecyclerViewDeleted(position)
        mView?.showUndoDeleteWord(position)
    }

    /**
     * Получаем все названия наборов слов
     * @return List<Sett>? список сетов
     */
    override fun onRightSwipe(): List<Sett>? {
        return mSettRepo.getAll()
    }


    /**
     * Сохранение набора слов
     * @param wordsDisplayed - текущий спсиок слов в наборе
     * @param wordsOriginal - изначальный список слов в наборе
     * @param sett - текущий набор слов (инфа)
     * @param inputLanguage - язык с которого нужен перевод
     * @param outputLanguage - язык на который нужно перевести
     * @param hasAutoSuggest - переменная наличия авто-перевода
     */
    override fun onSaveClicked(
        wordsDisplayed: List<Word?>,
        wordsOriginal: List<Word?>,
        sett: Sett?,
        inputLanguage: String,
        outputLanguage: String,
        hasAutoSuggest: Int
    ) {
        Log.d("wordsDisplayed", wordsDisplayed.toString())
        Log.d("wordsOriginal", wordsOriginal.toString())
//        Log.d("wordsEdited", wordsEdited.toString())
        val languageInputInfo = mLanguageRepo.getByTitle(inputLanguage.trim())
        val languageOutputInfo = mLanguageRepo.getByTitle(outputLanguage.trim())
        if (sett != null) {
            if (languageInputInfo != null) {
                if (languageInputInfo.languageId.isNotEmpty()) {
                    sett.languageInput_id = languageInputInfo.languageId
                } else {
                        mLanguageRepo.create(Language(languageTitle = inputLanguage.trim()))
                    sett.languageInput_id = inputLanguage.trim()
                }
            }

            sett.hasAutoSuggest = hasAutoSuggest
            sett.wordsAmount = wordsDisplayed.size

            if (languageOutputInfo != null) {
                if (languageOutputInfo.languageId.isNotEmpty()) {
                    sett.languageOutput_id = languageOutputInfo.languageId
                } else {
                    if (outputLanguage.trim() != inputLanguage.trim()) {
                            mLanguageRepo.create(Language(languageTitle = outputLanguage.trim()))
                        sett.languageOutput_id = outputLanguage.trim()
                    } else {
                        sett.languageOutput_id = sett.languageInput_id
                    }
                }
            }

            val settId = mSettRepo.update(sett)
            val wordsLeft = ArrayList(wordsOriginal)

            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())


            executor.execute {
                wordsDisplayed.forEachIndexed { index, word ->
                    val filtered = wordsOriginal.filter { it!!.wordId == word?.wordId }
                    if (filtered.isNullOrEmpty()) {
                        word!!.settId = settId
                        Log.d("element", word.toString())
                        val id = mWordRepo.create(word)
                        Log.d("createdElement id", id.toString())
                        wordsLeft.remove(word)
                    } else {
                        if (word!!.originalWord != (filtered[0]!!.originalWord) || word.originalWord != (filtered[0]!!.originalWord)) {
                            val id = mWordRepo.update(word)
                            Log.d("Word updated = ", id.toString())

                        }
                        wordsLeft.remove(filtered[0])
                    }
                }

                handler.post {
                }
            }


            executor.execute {
                if (wordsLeft.isNotEmpty()) {
                    wordsLeft.forEachIndexed { index, word ->
                        val wordId = mWordRepo.delete(word!!)
                    }

                }
                handler.post {

                }
            }

        }
    }


    override fun onViewCreated(settId: Long) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var result: List<Word>?
        var resultSet: Sett?
        executor.execute {
            result = mWordRepo.getWordsOfSet(settId = settId)
            resultSet = mSettRepo.get(settId)
            handler.post {
                if (resultSet != null) {
                    mView?.setSettData(resultSet!!)
                }
                mView?.setData(result)
            }
        }
    }

    override fun loadLanguagesData(sett: Sett) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var inputLanguage: String?
        var outputLanguage: String?
        executor.execute {
            inputLanguage = mLanguageRepo.getLanguageTitleLocale(sett.languageInput_id,Locale.getDefault().language)
            outputLanguage = mLanguageRepo.getLanguageTitleLocale(sett.languageOutput_id,Locale.getDefault().language)
            handler.post {
                if (inputLanguage != null && outputLanguage != null) {
                    mView?.setLanguageData(inputLanguage!!, outputLanguage!!)
                }
            }
        }
    }

    override fun onDestroy() {
        this.mView = null
    }
}


/*        wordsOriginal.forEachIndexed { index, element ->
            if (wordsEdited[index] != null) {
                Log.d("wordsEdited[$index]", wordsEdited[index].toString())
                Log.d("element", element.toString())
                if (wordsEdited[index] != element) {
                    WordUpdateAsyncTask(dbhelper).execute(wordsEdited[index])
                } else {
                        WordDeleteAsyncTask(dbhelper).execute(element)
                    }
                }
            }

        Log.d("wordsOriginal.size", wordsOriginal.size.toString())
        Log.d("wordsEdited.size", wordsEdited.size.toString())
        if (wordsOriginal.size < wordsEdited.size) {
            var createList = ArrayList<Word?>()
            for (i in wordsOriginal.size until wordsEdited.size) {
                wordsEdited[i]?.settId  = settId
                createList.add(wordsEdited[i])
            }
            WordCreateAsyncTask(dbhelper).execute(createList.filterNotNull() as ArrayList<Word>)
        }*/
/*    }

}


}*/


//        val wordCreateAsyncTask = WordCreateAsyncTask(dbhelper = dbhelper)
//        wordCreateAsyncTask.execute(wordsDisplayed as Any, settId as Any)
/* val wordId = mWordRepo.create(word)
 mSetWordRepo.create(SetWord(settId = settId, wordId = wordId))*/



