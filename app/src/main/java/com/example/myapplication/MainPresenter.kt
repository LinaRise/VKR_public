package com.example.myapplication

import com.example.myapplication.database.repo.studyProgress.StudyProgressRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.ui.DependencyInjector
import com.example.myapplication.ui.study.SetStudyContract

class MainPresenter(  view: MainContract.View,
    dependencyInjector: DependencyInjector
) : MainContract.Presenter {
    private var mView: MainContract.View? = view
    override fun onDestroy() {
        this.mView = null

    }

}