package com.example.myapplication.ui.setCreate

import com.example.myapplication.entity.Word

interface ISetCreateView {
    fun updateRecyclerViewInserted(word: Word)
    fun updateRecyclerViewDeleted(position: Int)
    fun showWordInputError()
    fun showUndoDeleteWord(position:Int)
    fun hideKeyboard()
    fun cleanInputFields()
    fun showToastMessage(line:String)
}