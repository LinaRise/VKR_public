package com.example.myapplication.ui.list

import com.example.myapplication.entity.Set
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.setCreate.ISetCreateView

class ListPagePresenter(view: IListPageView)  {
    private var mView: IListPageView = view
    private var set: Set = Set()

    var sets = ArrayList<Set>()

    fun loadData(){
        //пока просто список - будет обращение к бд

        var set1 = Set("111111111111111111111", "1","",1)
        var set2 = Set("2", "2","@",23)
        var set3 = Set("3111111111111", "3","23",23)
        var set4 = Set("4111111111", "4","234",345)
        var set5 = Set("5", "6","@34",345)
        sets.add(set1)
        sets.add(set2)
        sets.add(set3)
        sets.add(set4)
        sets.add(set5)

        mView.setData(sets)
    }

    fun openSet(){
        mView.openSetInfoActivity()
    }
}