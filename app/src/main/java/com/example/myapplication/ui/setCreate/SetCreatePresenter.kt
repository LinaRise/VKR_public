package com.example.myapplication.ui.setCreate

import com.example.myapplication.entity.Set
import com.example.myapplication.entity.Word

class SetCreatePresenter(view: ISetCreateView) {
    private var mView: ISetCreateView = view
    private var word: Word = Word()
    private var set: Set = Set()
    private var words = ArrayList<Word>()

    fun loadData(){
        //пока просто список - будет обращение к бд

        var word1 = Word("111111111111111111111", "1")
        var word2 = Word("2", "2")
        var word3 = Word("3111111111111", "3")
        var word4 = Word("4111111111", "4")
        var word5 = Word("5", "6")
        words.add(word1)
        words.add(word2)
        words.add(word3)
        words.add(word4)
        words.add(word5)

        mView.setData(words)
    }

    fun addNewWord(original: String, translated: String) {
        if (original == "" || translated == "") {
            mView.showWordInputError()
        }
        mView.hideKeyboard()
        val word =
            Word(original.trim(), translated.trim())
        //добавим здесь же в бд
        words.add(word)
        mView.cleanInputFields()
        mView.updateRecyclerViewInserted(word)
    }

    fun deleteWord(word:Word){
        //здесь будет удаление из бд - пока просто из списка
        val position = words.indexOf(word)
        words.removeAt(position)
        mView.updateRecyclerViewDeleted(position)
        mView.showUndoDeleteWord(position)

    }

}

