package com.example.myapplication.ui.setCreate.setUpDialog

import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView
import com.google.cloud.translate.Translate

class SetUpContract {
    interface Presenter : BasePresenter {
        fun onViewCreated(translate: Translate?)
    }

    interface View : BaseView<Presenter> {
        fun showNoInternetConnectionToast()
    }
}