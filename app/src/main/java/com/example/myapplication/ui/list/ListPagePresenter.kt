package com.example.myapplication.ui.list

import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.SettRepo
import com.example.myapplication.entity.Sett

class ListPagePresenter(view: IListPageView, dbhelper: DBHelper)  {
    private var mView: IListPageView = view
    private var set: Sett = Sett()

    private var sets = ArrayList<Sett>()
    var mSettRepo: SettRepo = SettRepo(dbhelper)
    fun loadData(){
        //обращение к бд
        val setList =  mSettRepo.getAll()

        if (setList!=null) {
            mView.setData(setList)
        }
        else{
           //показать сообщение, что спсиок сетов пустой
            mView.showMessage()
        }
    }

    fun openSet(){
        mView.openDialogForSetCreation()
    }
}