package com.example.myapplication.ui.list

import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SetDeleteAsyncTask
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word

class ListPagePresenter(view: IListPageView, var dbhelper: DBHelper) {
    private var mView: IListPageView = view
    private var set: Sett = Sett()

    private var sets = ArrayList<Sett>()
    var mSettRepo: SettRepo = SettRepo(dbhelper)
    var mLanguageRepo: LanguageRepo = LanguageRepo(dbhelper)
    fun loadData() {
        //обращение к бд
        val setList = mSettRepo.getAll()

        if (setList != null) {
            var languagesList = LinkedHashMap<Sett, List<String>>()
            for (sett in setList) {
                val inputLang = mLanguageRepo.get(sett.languageInput_id)
                val outputLang = mLanguageRepo.get(sett.languageOutput_id)
                var list = ArrayList<String>()
                if (inputLang != null)
                    list.add(inputLang.languageTitle)
                else
                    list.add("-")
                if (outputLang != null)
                    list.add(outputLang.languageTitle)
                else
                    list.add("-")
                languagesList[sett] = list
            }
            mView.setData(languagesList)

        } else {
            //показать сообщение, что спсиок сетов пустой
            mView.showMessage()
        }
    }

    fun openSet() {
        mView.openDialogForSetCreation()
    }

    fun deleteSettShow(sett: Sett, position: Int) {
        mView.updateRecyclerViewDeleted(position)
        mView.showUndoDeleteWord(position)

    }

    fun deleteSettFromDb(sett:Sett){
        SetDeleteAsyncTask(dbhelper).execute(sett)

    }

}