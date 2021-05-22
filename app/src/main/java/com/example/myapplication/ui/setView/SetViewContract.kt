package com.example.myapplication.ui.setView

import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView
import com.google.cloud.translate.Translate

interface SetViewContract {
    interface Presenter : BasePresenter {
        fun onViewCreated(settId: Long)
        fun onSaveClicked(
            wordsDisplayed: List<Word?>,
            wordsOriginal: List<Word?>,
            sett: Sett?,
            inputLanguage: String,
            outputLanguage: String,
            hasAutoSuggest: Int
        )

        fun onLeftSwipe(position: Int)
        fun onRightSwipe(): List<Sett>?
        fun onTranslate(
            translate: Translate,
            languageTitleAndCode: Map<String, String>,
            originalText: String,
            sourceLanguage: String,
            targetLanguage: String
        ): String

        fun onAddWordClicked(original: String, translated: String)
        fun loadLanguagesData(sett: Sett)
    }

    interface View : BaseView<Presenter> {
        fun updateRecyclerViewInserted(word: Word)
        fun updateRecyclerViewDeleted(position: Int)
        fun showWordInputError()
        fun showUndoDeleteWord(position: Int)
        fun hideKeyboard()
        fun cleanInputFields()
        fun setData(result: List<Word>?)
        fun showToast(message: String)
        fun showDialog(sets: List<Sett>?, position: Int)
        fun setSettData(resultSett:Sett)
        fun setLanguageData(inputLang: Language, outputLanguage:Language)
    }
}
