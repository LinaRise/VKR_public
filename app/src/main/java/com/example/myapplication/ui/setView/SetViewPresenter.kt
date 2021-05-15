package com.example.myapplication.ui.setView

import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.*
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.translation.TranslationUtils
import com.google.cloud.translate.Translate

class SetViewPresenter(
    view: ISetViewView,
    private var dbhelper: DBHelper
) {
    private var mView: ISetViewView = view
//    private var words = ArrayList<Word>()

    var mLanguageRepo: LanguageRepo = LanguageRepo(dbhelper)
    var mWordRepo: WordRepo = WordRepo(dbhelper)
    var mSettRepo: SettRepo = SettRepo(dbhelper)
//    var mSetWordRepo: SetWordRepo = SetWordRepo(dbhelper)

    //добавление нового слова
    fun addNewWord(original: String, translated: String) {
        if (original == "" || translated == "") {
            mView.showWordInputError()
            return
        }
        mView.hideKeyboard()
        val word = Word(0, original.trim(), translated.trim())
        mView.cleanInputFields()
        mView.updateRecyclerViewInserted(word)
    }

    fun translate(translate: Translate,
                  languageTitleAndCode: Map<String, String>,
                  originalText: String,
                  sourceLanguage: String,
                  targetLanguage: String): String {
      return  TranslationUtils.translate(translate,languageTitleAndCode,originalText,sourceLanguage,targetLanguage)

    }



    //удаление слова из списка
    fun deleteWord(word: Word, position: Int) {
        mView.updateRecyclerViewDeleted(position)
        mView.showUndoDeleteWord(position)
    }

    fun getAllSetsTitles(): List<Sett>? {
        return mSettRepo.getAll()
    }

    fun onDoneButtonWasClicked(
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
                if (languageInputInfo.languageId != 0L) {
                    sett.languageInput_id = languageInputInfo.languageId
                } else {
                    val newInputLangId =
                        mLanguageRepo.create(Language(languageTitle = inputLanguage.trim()))
                    sett.languageInput_id = newInputLangId
                }
            }

            sett.hasAutoSuggest = hasAutoSuggest
            sett.wordsAmount = wordsDisplayed.size

            if (languageOutputInfo != null) {
                if (languageOutputInfo.languageId != 0L) {
                    sett.languageOutput_id = languageOutputInfo.languageId
                } else {
                    if (outputLanguage.trim() != inputLanguage.trim()) {
                        val newOutputLangId =
                            mLanguageRepo.create(Language(languageTitle = outputLanguage.trim()))
                        sett.languageOutput_id = newOutputLangId
                    } else {
                        sett.languageOutput_id = sett.languageInput_id
                    }
                }
            }

            val settId = mSettRepo.update(sett)
            var wordsLeft = ArrayList(wordsOriginal)
            wordsDisplayed.forEachIndexed { index, element ->
                val filtered = wordsOriginal.filter { it!!.wordId == element?.wordId }
                if (filtered.isNullOrEmpty()) {
                    element!!.settId = settId
                    Log.d("element", element.toString())
                    val id = mWordRepo.create(element)
                    Log.d("createdElement id", id.toString())
                   /* val id = WordCreateAsyncTask(dbhelper).execute(element)*/
                    wordsLeft.remove(element)
                } else {
                    if (element!!.originalWord != (filtered[0]!!.originalWord) || element.originalWord != (filtered[0]!!.originalWord)) {
                        val id = WordUpdateAsyncTask(dbhelper).execute(element)
                        Log.d("Word updated = ", id.toString())

                    }
                    wordsLeft.remove(filtered[0])
                }
            }

            if (wordsLeft.isNotEmpty()) {
                wordsLeft.forEachIndexed { index, element ->
                    WordDeleteAsyncTask(dbhelper).execute(element)
                }

/*
            if (element !in wordsOriginal) {
                if (element?.wordId != null) {
                    val filtered = wordsOriginal.filter { it!!.wordId == element.wordId }
                    if (filtered.isNotEmpty()) {
                        WordUpdateAsyncTask(dbhelper).execute(filtered[0])
                    } else {
                        WordDeleteAsyncTask(dbhelper).execute(element)
                    }
                } else {
                    WordCreateAsyncTask(dbhelper).execute(element)
                }
            }*/
            }
        }
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



