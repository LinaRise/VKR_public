package com.example.myapplication.ui.setCreate

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word

class SetCreatePresenter(
    view: ISetCreateView,
    dbhelper: DBHelper
) {
    private var mView: ISetCreateView = view

    var mRepository: SetCreateRepo = SetCreateRepo(dbhelper)
    private var word: Word = Word()
    private var set: Sett = Sett()
    private var words = ArrayList<Word>()


//    fun loadData() {
//        //пока просто список - будет обращение к бд
//
//        var languageInputInfo = db.rawQuery(
//            "SELECT * FROM ${TablesAndColumns.LanguageEntry.TABLE_NAME} " +
//                    "WHERE ${TablesAndColumns.LanguageEntry.COL_LANGUAGE_TITLE} = $inputLanguage",
//            null
//        );
//
//    }

    fun onDoneButtonWasClicked(
        wordsDisplayed: List<Word>, setTitle: String, inputLanguage: String,
        outputLanguage: String
    ) {
        Log.e("SetCreate", inputLanguage.trim())
        val languageInputInfo = mRepository.getLanguageByTitle(inputLanguage.trim())
        val languageOutputInfo = mRepository.getLanguageByTitle(outputLanguage.trim())
        Log.e("SetCreate", languageInputInfo?.languageId.toString())
        Log.e("SetCreate", languageOutputInfo?.languageId.toString())
        Log.e("SetCreate", languageInputInfo!!.languageId.toString())

        var newSet = Sett(0, setTitle, wordsAmount = wordsDisplayed.size)
        if (languageInputInfo != null) {
            if (languageInputInfo.languageId != 0L) {
                newSet.languageInput_id = languageInputInfo.languageId
            } else {
                val newInputLangId = mRepository.addLanguage(inputLanguage)
                Log.e("SetCreate!", languageOutputInfo?.languageTitle.toString())
                Log.e("SetCreate!", newInputLangId.toString())
                newSet.languageInput_id = newInputLangId
            }
        }
        if (languageOutputInfo != null) {
            if (languageOutputInfo.languageId != 0L) {
                newSet.languageOutput_id = languageOutputInfo.languageId
            } else {
                if (outputLanguage.trim() != inputLanguage.trim()) {
                    val newOutputLangId = mRepository.addLanguage(outputLanguage)
                    newSet.languageOutput_id = newOutputLangId
                }
                else{
                    newSet.languageOutput_id = newSet.languageInput_id
                }
            }
        }

        val setId = mRepository.addSet(newSet)

        for (word in wordsDisplayed) {
            val wordId = mRepository.addWord(word)
            mRepository.bindSetWord(wordId = wordId, settId = setId)
        }
    }

    fun addNewWord(original: String, translated: String) {
        if (original == "" || translated == "") {
            mView.showWordInputError()
        }
        mView.hideKeyboard()
        val word =
            Word(0, original.trim(), translated.trim())

        words.add(word)
        mView.cleanInputFields()
        mView.updateRecyclerViewInserted(word)
    }

    fun deleteWord(word: Word) {
        //здесь будет удаление из бд - пока просто из списка
        val position = words.indexOf(word)
        words.removeAt(position)
        mView.updateRecyclerViewDeleted(position)
        mView.showUndoDeleteWord(position)

    }
//
//     fun insertDataToDatabase(words: List<Word>) {
//        for (word in words){
//            mWordViewModel.insert(word)
//        }
//    }

}

