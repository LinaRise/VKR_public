package com.example.myapplication.ui.chathead

import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView
import com.google.cloud.translate.Translate

interface TranslateBubbleContract {
    interface Presenter : BasePresenter {
       fun getAllSetsTitles(): List<Sett>?
    }

    interface View : BaseView<Presenter> {

    }
}
