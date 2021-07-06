package com.example.myapplication.ui.study

import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.StudyProgress
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView
import com.google.cloud.translate.Translate

interface SetStudyContract {
    interface Presenter : BasePresenter {
        fun updateWordsPoints(list: List<Word>)
        fun updateStudyProgress(studyProgress: StudyProgress)
    }

    interface View : BaseView<Presenter> {

    }
}
