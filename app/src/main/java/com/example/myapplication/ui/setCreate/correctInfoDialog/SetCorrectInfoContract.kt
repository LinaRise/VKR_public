package com.example.myapplication.ui.setCreate.correctInfoDialog

import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView
import com.google.cloud.translate.Translate

interface SetCorrectInfoContract {
    interface Presenter : BasePresenter {
        fun onViewCreated(translate: Translate?)

    }

    interface View : BaseView<Presenter> {
        fun showNoInternetConnectionToast()
        fun setAvailableLanguagesInfo(languageCodeAndTitle: Map<String, String>)
    }
}