package com.example.myapplication.ui.setView

import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.*
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word

class SetViewPresenter(
    view: ISetViewView,
    dbhelper: DBHelper
) {
    private var dbhelper = dbhelper
    private var mView: ISetViewView = view
//    private var words = ArrayList<Word>()

    var mLanguageRepo: LanguageRepo = LanguageRepo(dbhelper)
    var mWordRepo: WordRepo = WordRepo(dbhelper)
    var mSettRepo: SettRepo = SettRepo(dbhelper)
//    var mSetWordRepo: SetWordRepo = SetWordRepo(dbhelper)

    fun addNewWord(original: String, translated: String) {
        if (original == "" || translated == "") {
            mView.showWordInputError()
            return
        }
        mView.hideKeyboard()
        val word = Word(0, original.trim(), translated.trim())
//        words.add(word)
        mView.cleanInputFields()
        mView.updateRecyclerViewInserted(word)
    }

    fun deleteWord(word: Word, position: Int) {
        //здесь будет удаление из бд - пока просто из списка
//        val position = words.indexOf(word)
//        words.removeAt(position)
        mView.updateRecyclerViewDeleted(position)
        mView.showUndoDeleteWord(position)

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
                    element!!.settId  = settId
                    WordCreateAsyncTask(dbhelper).execute(element)
                    wordsLeft.remove(element)
                } else {
                    if (element!!.originalWord!=(filtered[0]!!.originalWord) || element.originalWord!=(filtered[0]!!.originalWord) )
                        WordUpdateAsyncTask(dbhelper).execute(element)
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



