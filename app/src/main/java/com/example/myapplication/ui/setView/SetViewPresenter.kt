package com.example.myapplication.ui.setView

import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.setword.SetWordRepo
import com.example.myapplication.database.repo.word.WordCreateAsyncTask
import com.example.myapplication.database.repo.word.WordDeleteAsyncTask
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.database.repo.word.WordUpdateAsyncTask
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
    var mSetWordRepo: SetWordRepo = SetWordRepo(dbhelper)

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

    fun deleteWord(word: Word, position:Int) {
        //здесь будет удаление из бд - пока просто из списка
//        val position = words.indexOf(word)
//        words.removeAt(position)
        mView.updateRecyclerViewDeleted(position)
        mView.showUndoDeleteWord(position)

    }

    fun onDoneButtonWasClicked(
        wordsDisplayed: List<Word?>,
        wordsOriginal: List<Word?>,
        wordsEdited: List<Word?>,
        sett: Sett?,
        inputLanguage: String,
        outputLanguage: String,
        hasAutoSuggest: Int
    ) {

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

            wordsOriginal.forEachIndexed { index, element ->
                if (wordsEdited[index] != null) {
                    Log.d("wordsEdited[$index]", wordsEdited[index].toString())
                    Log.d("element", element.toString())
                    if (wordsEdited[index] != element) {

                        WordUpdateAsyncTask(dbhelper).execute(wordsEdited[index])
                    }
                } else {
                    if (element != null) {

                        WordDeleteAsyncTask(dbhelper).execute(sett.settId as Any,element as Any)
                    }
                }


            }
            Log.d("wordsOriginal.size", wordsOriginal.size.toString())
            Log.d("wordsEdited.size", wordsEdited.size.toString())
            if (wordsOriginal.size < wordsEdited.size) {
                var createList = ArrayList<Word?>()
                for (i in wordsOriginal.size until wordsEdited.size) {
                    createList.add(wordsEdited[i])
                }
                WordCreateAsyncTask(dbhelper).execute(createList.filterNotNull() as Any,sett.settId as Any)
            }
        }

    }


}


//        val wordCreateAsyncTask = WordCreateAsyncTask(dbhelper = dbhelper)
//        wordCreateAsyncTask.execute(wordsDisplayed as Any, settId as Any)
/* val wordId = mWordRepo.create(word)
 mSetWordRepo.create(SetWord(settId = settId, wordId = wordId))*/



