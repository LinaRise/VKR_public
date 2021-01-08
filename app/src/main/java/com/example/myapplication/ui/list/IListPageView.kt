package com.example.myapplication.ui.list

import com.example.myapplication.entity.Set

interface IListPageView {
    fun setData(sets: List<Set>)
    fun openSetInfoActivity()
}