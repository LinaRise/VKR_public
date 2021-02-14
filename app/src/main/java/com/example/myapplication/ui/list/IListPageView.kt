package com.example.myapplication.ui.list

import com.example.myapplication.entity.Sett

interface IListPageView {
    fun setData(setsInfo: LinkedHashMap <Sett, List<String>>)
    fun openDialogForSetCreation()
    fun showMessage()
    fun updateRecyclerViewDeleted(position: Int)
    fun showUndoDeleteWord(position: Int)
}