package com.example.myapplication.ui.chathead

import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Sett

class ChatActivityPresenter(
    dbhelper: DBHelper
) {
    var mWordRepo: WordRepo = WordRepo(dbhelper)
    var mSettRepo: SettRepo = SettRepo(dbhelper)

    fun getAllSetsTitles(): List<Sett>? {
        return mSettRepo.getAll()
    }
}