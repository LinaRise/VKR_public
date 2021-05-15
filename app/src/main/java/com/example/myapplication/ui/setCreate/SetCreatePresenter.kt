package com.example.myapplication.ui.setCreate

import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordCreateManyAsyncTask
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.translation.TranslationUtils
import com.google.cloud.translate.Translate

class SetCreatePresenter(
    view: ISetCreateView,
    private var dbhelper: DBHelper
) {
    private var mView: ISetCreateView = view
    var mLanguageRepo: LanguageRepo = LanguageRepo(dbhelper)
    var mWordRepo: WordRepo = WordRepo(dbhelper)
    var mSettRepo: SettRepo = SettRepo(dbhelper)
    private var word: Word = Word()
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
    fun translate(translate: Translate,
                  languageTitleAndCode: Map<String, String>,
                  originalText: String,
                  sourceLanguage: String,
                  targetLanguage: String): String {
        return  TranslationUtils.translate(translate,languageTitleAndCode,originalText,sourceLanguage,targetLanguage)

    }
    /**
     * Сохранение набора слов
     * @param wordsDisplayed - текущий спсиок слов в наборе
     * @param setTitle - навзание для перевода
     * @param inputLanguage - язык с которого нужен перевод
     * @param outputLanguage - язык на который нужно перевести
     * @param hasAutoSuggest - переменная наличия авто-перевода
     */
    fun saveSet(
        wordsDisplayed: List<Word>, setTitle: String, inputLanguage: String,
        outputLanguage: String, hasAutoSuggest: Int
    ) {
        val languageInputInfo = mLanguageRepo.getByTitle(inputLanguage.trim())
        val languageOutputInfo = mLanguageRepo.getByTitle(outputLanguage.trim())
        var newSet = Sett(
            0,
            setTitle.trim(),
            wordsAmount = wordsDisplayed.size,
            hasAutoSuggest = hasAutoSuggest
        )
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

        for (word in wordsDisplayed) {
            word.settId = settId
        }
        WordCreateManyAsyncTask(dbhelper).execute(wordsDisplayed)


    }

    /**
     * добавление слова
     */
    fun addNewWord(original: String, translated: String) {
        if (original == "" || translated == "") {
            mView.showWordInputError()
            return
        }
        mView.hideKeyboard()
        val word =
            Word(0, original.trim(), translated.trim())

        mView.cleanInputFields()
        mView.updateRecyclerViewInserted(word)
    }
    /**
     * удаление слова
     */
    fun deleteWord( position: Int) {
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

