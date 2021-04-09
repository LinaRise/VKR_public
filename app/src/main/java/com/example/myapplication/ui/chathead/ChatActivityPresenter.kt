package com.example.myapplication.ui.chathead

import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Sett
import com.example.myapplication.ui.setView.ISetViewView

class ChatActivityPresenter(view: IChatActivityView,
                           private var dbhelper: DBHelper
) {
    private var mView: IChatActivityView = view
    var mLanguageRepo: LanguageRepo = LanguageRepo(dbhelper)
    var mWordRepo: WordRepo = WordRepo(dbhelper)
    var mSettRepo: SettRepo = SettRepo(dbhelper)


    fun getAllSetsTitles(): List<Sett>? {
        return mSettRepo.getAll()
    }
}