package com.example.myapplication.ui.setCreate

import android.os.Handler
import android.os.Looper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.translation.TranslationUtils
import com.example.myapplication.ui.DependencyInjector
import com.google.cloud.translate.Translate
import java.util.concurrent.Executors

class SetCreatePresenter(
    view: SetCreateContract.View,
    dependencyInjector: DependencyInjector
) : SetCreateContract.Presenter {
    private var mView: SetCreateContract.View? = view
    private val mSettRepo: SettRepo = dependencyInjector.settRepository()
    private val mLanguageRepo: LanguageRepo = dependencyInjector.languageRepository()
    private val mWordRepo: WordRepo = dependencyInjector.wordRepository()
    private var set: Sett = Sett()


    /**
     * Перевод слова
     * @param translate - переменная сервиса перевода
     * @param languageTitleAndCode - код и название языков
     * @param originalText - текст для перевода
     * @param sourceLanguage - язык с которого нужен перевод
     * @param targetLanguage - язык на который нужно перевести
     * @return String перевод
     */
    override fun onTranslate(
        translate: Translate,
        languageTitleAndCode: Map<String, String>,
        originalText: String,
        sourceLanguage: String,
        targetLanguage: String,
        hasAutoSuggest: Int,
        hasInternet: Boolean
    ): String {
        if (hasAutoSuggest == 1) {
            if (hasInternet) {
                return TranslationUtils.translate(
                    translate,
                    languageTitleAndCode,
                    originalText,
                    sourceLanguage,
                    targetLanguage
                )
            }
        }
        return ""
    }

    /**
     * Сохранение набора слов
     * @param wordsDisplayed - текущий спсиок слов в наборе
     * @param setTitle - навзание для перевода
     * @param inputLanguage - язык с которого нужен перевод
     * @param outputLanguage - язык на который нужно перевести
     * @param hasAutoSuggest - переменная наличия авто-перевода
     */
    override fun onSaveClicked(
        wordsDisplayed: List<Word>, setTitle: String, inputLanguage: String,
        outputLanguage: String, hasAutoSuggest: Int
    ) {
        val languageInputInfo = mLanguageRepo.getByLocaleTitle(inputLanguage.trim())
        val languageOutputInfo = mLanguageRepo.getByLocaleTitle(outputLanguage.trim())
        val newSet = Sett(
            0,
            setTitle.trim(),
            wordsAmount = wordsDisplayed.size,
            hasAutoSuggest = hasAutoSuggest
        )
        if (languageInputInfo != null) {
            if (languageInputInfo.languageId.isNotEmpty()) {
                newSet.languageInput_id = languageInputInfo.languageId
            } else {
                    mLanguageRepo.create(Language(languageTitle = inputLanguage.trim()))
                newSet.languageInput_id = inputLanguage.trim()
            }
        }
        if (languageOutputInfo != null) {
            if (languageOutputInfo.languageId.isNotEmpty()) {
                newSet.languageOutput_id = languageOutputInfo.languageId
            } else {
                if (outputLanguage.trim() != inputLanguage.trim()) {
                        mLanguageRepo.create(Language(languageTitle = outputLanguage.trim()))
                    newSet.languageOutput_id = outputLanguage.trim()
                } else {
                    newSet.languageOutput_id = newSet.languageInput_id
                }
            }
        }

        val settId = mSettRepo.create(newSet)

        for (word in wordsDisplayed) {
            word.settId = settId
        }

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            for (word in wordsDisplayed) {
                val wordId = mWordRepo.create(word)
            }

            handler.post {
                mView?.showSuccessSavedToast()
            }
        }

    }

    /**
     * добавление слова
     */
    override fun onAddWordClicked(original: String, translated: String) {
        if (original == "" || translated == "") {
            mView?.showWordInputError()
            return
        }
        mView?.hideKeyboard()
        val word =
            Word(0, original.trim(), translated.trim())

        mView?.cleanInputFields()
        mView?.updateRecyclerViewInserted(word)
    }

    /**
     * удаление слова
     */
    override fun onLeftSwipe(position: Int) {
        mView?.updateRecyclerViewDeleted(position)
        mView?.showUndoDeleteWord(position)

    }


    override fun onDestroy() {
        this.mView = null
    }



}

