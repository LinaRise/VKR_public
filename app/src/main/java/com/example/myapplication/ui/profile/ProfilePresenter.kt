package com.example.myapplication.ui.profile

import android.os.Handler
import android.os.Looper
import com.example.myapplication.database.repo.studyProgress.StudyProgressRepo
import com.example.myapplication.entity.StudyProgress
import com.example.myapplication.ui.DependencyInjector
import com.github.mikephil.charting.data.BarEntry
import java.util.concurrent.Executors

class ProfilePresenter(
    view: ProfileContract.View,
    dependencyInjector: DependencyInjector
) : ProfileContract.Presenter {

    private var mView: ProfileContract.View? = view
    var mStudyProgressRepo: StudyProgressRepo = dependencyInjector.studyProgressRepository()


    override fun onViewCreated() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var list: List<StudyProgress>?
        executor.execute {
            list = mStudyProgressRepo.getAll()
            handler.post {
                if (list != null) {
                    if (list!!.isNotEmpty()){
                        var values: ArrayList<BarEntry> = ArrayList()
                        var labels: ArrayList<String> = ArrayList()
                        for (i in list!!.indices) {
                            values.add(
                                BarEntry(
                                    (list!!.size - i-1).toFloat(),
                                    ((list!![i].rightAnswers.toFloat() * 100) / (list!![i].rightAnswers.toFloat() + list!![i].wrongAnswers.toFloat()))
                                )
                            )
                            labels.add(list!![i].date.toString().split("-", limit = 2)[1])
                        }
                        mView?.setData(list!!,values.reversed(),labels.reversed())
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        this.mView = null
    }


}