package com.example.myapplication.ui.list

import android.os.Handler
import android.os.Looper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.entity.Sett
import com.example.myapplication.ui.DependencyInjector
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class ListPagePresenter(view: ListPageContract.View, dependencyInjector: DependencyInjector) :
    ListPageContract.Presenter {
    //    private var mView: IListPageView = view
    private var set: Sett = Sett()
    private var mView: ListPageContract.View? = view
    private val mSettRepo: SettRepo = dependencyInjector.settRepository()
    private val mLanguageRepo: LanguageRepo = dependencyInjector.languageRepository()
    /*  var mSettRepo: SettRepo = SettRepo(dbhelper)
      var mLanguageRepo: LanguageRepo = LanguageRepo(dbhelper)*/

    /**
     * получение данных из бд
     */
    override fun onViewCreated() {
        loadSettsList()
    }


    private fun loadSettsList() {
        //обращение к бд
        val setList = mSettRepo.getAll()
        if (setList != null) {
            val languagesList = LinkedHashMap<Sett, List<String>>()
            for (sett in setList) {
                val inputLang = mLanguageRepo.getLanguageTitleLocale(sett.languageInput_id,Locale.getDefault().language)
                val outputLang = mLanguageRepo.getLanguageTitleLocale(sett.languageOutput_id,Locale.getDefault().language)
                val list = ArrayList<String>()
                list.add(inputLang)
                list.add(outputLang)
                languagesList[sett] = list
            }
            mView?.setData(languagesList)

        } else {
            mView?.showMessage()
        }
    }

    /**
     * открытие диалога создания сет слов
     */
    override fun onCreateSettTapped() {
        mView?.openDialog()

    }


    /**
     * удалени набора слов из списка
     */
    override fun onLeftSwipe(position: Int) {
        mView?.updateRecyclerViewDeleted(position)
        mView?.showUndoDeleteWord(position)

    }

    /**
     * удаление набора слов из БД
     */
    override fun onSettDelete(sett: Sett) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            val wordId = mSettRepo.delete(sett)
        }
    }

    override fun onDestroy() {
        this.mView = null
    }

}