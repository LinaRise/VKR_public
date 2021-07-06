package com.example.myapplication.ui.chathead

import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Sett
import com.example.myapplication.ui.DependencyInjector
import com.example.myapplication.ui.setCreate.setUpDialog.SetUpContract

class TranslateBubblePresenter(
    view: TranslateBubbleContract.View,
    dependencyInjector: DependencyInjector
) : TranslateBubbleContract.Presenter {
    private var mView: TranslateBubbleContract.View? = view

    var mSettRepo: SettRepo = dependencyInjector.settRepository()

    override fun getAllSetsTitles(): List<Sett>? {
        return mSettRepo.getAll()
    }

    override fun onDestroy() {
        this.mView = null
    }
}