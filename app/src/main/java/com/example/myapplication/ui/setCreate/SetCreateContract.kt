package com.example.myapplication.ui.setCreate

import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView
import com.google.cloud.translate.Translate

interface SetCreateContract {
    interface Presenter : BasePresenter {
        fun onSaveClicked(
            wordsDisplayed: List<Word>, setTitle: String, inputLanguage: String,
            outputLanguage: String, hasAutoSuggest: Int
        )

        fun onLeftSwipe(position: Int)
        fun onTranslate(
            translate: Translate,
            languageTitleAndCode: Map<String, String>,
            originalText: String,
            sourceLanguage: String,
            targetLanguage: String,
            hasAutoSuggest: Int,
            hasInternet: Boolean
        ): String

        fun onAddWordClicked(original: String, translated: String)
    }

    interface View : BaseView<Presenter> {
        fun updateRecyclerViewInserted(word: Word)
        fun updateRecyclerViewDeleted(position: Int)
        fun showWordInputError()
        fun showUndoDeleteWord(position: Int)
        fun hideKeyboard()
        fun cleanInputFields()
        fun showSuccessSavedToast()
    }
}
