package com.example.myapplication.ui.list

import com.example.myapplication.entity.Sett

interface IListPageView {
    fun setData(sets: List<Sett>)
    fun openDialogForSetCreation()
}