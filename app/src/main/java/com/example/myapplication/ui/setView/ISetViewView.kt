package com.example.myapplication.ui.setView

import com.example.myapplication.entity.Word

interface ISetViewView {
//    fun setData(words: List<Word>)
    fun updateRecyclerViewInserted(word: Word)
    fun updateRecyclerViewDeleted(position: Int)
    fun showWordInputError()
    fun showUndoDeleteWord(position:Int)
    fun hideKeyboard()
    fun cleanInputFields()
    fun setData(result: List<Word>?)
}