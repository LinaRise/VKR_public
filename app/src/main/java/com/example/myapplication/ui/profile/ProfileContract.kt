package com.example.myapplication.ui.profile

import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.StudyProgress
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView
import com.google.cloud.translate.Translate

interface ProfileContract {
    interface Presenter : BasePresenter {
       fun onViewCreated()
    }

    interface View : BaseView<Presenter> {
        fun setData(list: List<StudyProgress>)

    }
}
