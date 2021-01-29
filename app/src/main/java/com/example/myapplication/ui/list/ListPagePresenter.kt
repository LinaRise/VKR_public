package com.example.myapplication.ui.list

import com.example.myapplication.entity.Sett

class ListPagePresenter(view: IListPageView)  {
    private var mView: IListPageView = view
    private var set: Sett = Sett()

    private var sets = ArrayList<Sett>()

    fun loadData(){
        //пока просто список - будет обращение к бд

        var set1 = Sett(1, "1",1,1)
        var set2 = Sett(2, "2",1,23)
        var set3 = Sett(2, "3",2,23)

        sets.add(set1)
        sets.add(set2)
        sets.add(set3)

        mView.setData(sets)
    }

    fun openSet(){
        mView.openDialogForSetCreation()
    }
}