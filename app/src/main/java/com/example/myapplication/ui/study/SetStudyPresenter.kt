package com.example.myapplication.ui.study

import android.os.Handler
import android.os.Looper
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.setView.ISetViewView
import java.util.concurrent.Executors

class SetStudyPresenter(view: ISetStudyView,
                        dbhelper: DBHelper
) {
    private var mView: ISetStudyView = view

    var mWordRepo: WordRepo = WordRepo(dbhelper)
    fun updateWordsPoints(list:List<Word>){
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            for (word in list) {
                mWordRepo.update(word)
            }
            handler.post {
                /*
                * You can perform any operation that
                * requires UI Thread here.
                *
                * its like onPostExecute()
                * */
            }
        }
    }
}