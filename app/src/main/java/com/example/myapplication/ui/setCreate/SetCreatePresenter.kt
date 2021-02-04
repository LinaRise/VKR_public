package com.example.myapplication.ui.setCreate

import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.setword.SetWordRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordCreateAsyncTask
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import kotlin.collections.ArrayList

class SetCreatePresenter(
    view: ISetCreateView,
    dbhelper: DBHelper
) {
    private var mView: ISetCreateView = view
    private var dbhelper = dbhelper
    var mLanguageRepo: LanguageRepo = LanguageRepo(dbhelper)
    var mWordRepo: WordRepo = WordRepo(dbhelper)
    var mSettRepo: SettRepo = SettRepo(dbhelper)
    var mSetWordRepo: SetWordRepo = SetWordRepo(dbhelper)
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
        outputLanguage: String, hasAutoSuggest:Int
    ) {
        val languageInputInfo = mLanguageRepo.getByTitle(inputLanguage.trim())
        val languageOutputInfo = mLanguageRepo.getByTitle(outputLanguage.trim())
        var newSet = Sett(0, setTitle.trim(), wordsAmount = wordsDisplayed.size, hasAutoSuggest = hasAutoSuggest)
        if (languageInputInfo != null) {
            if (languageInputInfo.languageId != 0L) {
                newSet.languageInput_id = languageInputInfo.languageId
            } else {
                val newInputLangId =
                    mLanguageRepo.create(Language(languageTitle = inputLanguage.trim()))
                newSet.languageInput_id = newInputLangId
            }
        }
        if (languageOutputInfo != null) {
            if (languageOutputInfo.languageId != 0L) {
                newSet.languageOutput_id = languageOutputInfo.languageId
            } else {
                if (outputLanguage.trim() != inputLanguage.trim()) {
                    val newOutputLangId =
                        mLanguageRepo.create(Language(languageTitle = outputLanguage.trim()))
                    newSet.languageOutput_id = newOutputLangId
                } else {
                    newSet.languageOutput_id = newSet.languageInput_id
                }
            }
        }

        val settId = mSettRepo.create(newSet)


        val wordCreateAsyncTask = WordCreateAsyncTask(dbhelper = dbhelper)
        wordCreateAsyncTask.execute(wordsDisplayed as Any, settId as Any)
           /* val wordId = mWordRepo.create(word)
            mSetWordRepo.create(SetWord(settId = settId, wordId = wordId))*/

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

