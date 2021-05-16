package com.example.myapplication.ui.profile

import android.os.Handler
import android.os.Looper
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.studyProgress.StudyProgressRepo
import com.example.myapplication.entity.StudyProgress
import java.util.concurrent.Executors

class ProfilePresenter(
    view: IProfileFragmentView,
    dbhelper: DBHelper
) {

    private var mView: IProfileFragmentView = view
    var mStudyProgress: StudyProgressRepo = StudyProgressRepo(dbhelper)

    fun loadStudyProgressData() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var list: List<StudyProgress>
        executor.execute {
            list = mStudyProgress.getAll()
            handler.post {
                mView.setData(list)
            }
        }
    }


}