package com.example.myapplication.ui.list

import android.os.Handler
import android.os.Looper
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.entity.Sett
import java.util.concurrent.Executors

class ListPagePresenter(view: IListPageView, var dbhelper: DBHelper) {
    private var mView: IListPageView = view
    private var set: Sett = Sett()

    private var sets = ArrayList<Sett>()
    var mSettRepo: SettRepo = SettRepo(dbhelper)
    var mLanguageRepo: LanguageRepo = LanguageRepo(dbhelper)

    /**
     * получение данных из бд
     */
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

    /**
     * открытие диалога создания сет слов
     */
    fun openSet() {
        mView.openDialogForSetCreation()
    }

    /**
     * удалени набора слов из списка
     */
    fun deleteSettShow(position: Int) {
        mView.updateRecyclerViewDeleted(position)
        mView.showUndoDeleteWord(position)

    }
    /**
     * удаление набора слов из БД
     */
    fun deleteSettFromDb(sett:Sett){
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            val wordId = mSettRepo.delete(sett)
        }
    }

}