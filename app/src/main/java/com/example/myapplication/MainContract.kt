package com.example.myapplication

import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.StudyProgress
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView
import com.google.cloud.translate.Translate

interface MainContract {
    interface Presenter : BasePresenter {

    }

    interface View : BaseView<Presenter> {
        fun showDrawOverAppPermission()
        fun showTranslateBubbleOn()
        fun showTranslateBubbleOff()

    }
}
