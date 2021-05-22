package com.example.myapplication.ui.profile

import android.os.Handler
import android.os.Looper
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.studyProgress.StudyProgressRepo
import com.example.myapplication.entity.StudyProgress
import com.example.myapplication.ui.DependencyInjector
import com.example.myapplication.ui.study.SetStudyContract
import java.util.concurrent.Executors

class ProfilePresenter(
    view: ProfileContract.View,
    dependencyInjector: DependencyInjector
):ProfileContract.Presenter {

    private var mView: ProfileContract.View? = view
    var mStudyProgressRepo: StudyProgressRepo = dependencyInjector.studyProgressRepository()


    override fun onViewCreated() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var list: List<StudyProgress>
        executor.execute {
            list = mStudyProgressRepo.getAll()
            handler.post {
                mView?.setData(list)
            }
        }
    }

    override fun onDestroy() {
        this.mView = null
    }


}