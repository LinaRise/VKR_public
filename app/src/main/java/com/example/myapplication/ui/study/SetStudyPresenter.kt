package com.example.myapplication.ui.study

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.studyProgress.StudyProgressRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.StudyProgress
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.DependencyInjector
import com.example.myapplication.ui.setView.SetViewContract
import java.time.LocalDate
import java.util.concurrent.Executors

class SetStudyPresenter(
    view: SetStudyContract.View,
    dependencyInjector: DependencyInjector
) : SetStudyContract.Presenter {
    private var mView: SetStudyContract.View? = view
    private val mWordRepo: WordRepo = dependencyInjector.wordRepository()
    var mStudyProgressRepo: StudyProgressRepo = dependencyInjector.studyProgressRepository()

    override fun updateWordsPoints(list: List<Word>) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            for (word in list) {
                mWordRepo.update(word)
            }

        }
    }

    override fun updateStudyProgress(studyProgress: StudyProgress) {
        val executor = Executors.newSingleThreadExecutor()
        Log.d("updateStudyProgress", studyProgress.wrongAnswers.toString())
        Log.d("updateStudyProgress2", studyProgress.rightAnswers.toString())
        executor.execute {
            val currentDateProgress = mStudyProgressRepo.get(studyProgress.date)
            if (currentDateProgress.date != LocalDate.parse("2018-12-12")) {
                Log.d("currentDateProgress", currentDateProgress.rightAnswers.toString())
                Log.d("currentDateProgress2", currentDateProgress.wrongAnswers.toString())

                studyProgress.rightAnswers =
                    studyProgress.rightAnswers + currentDateProgress.rightAnswers
                studyProgress.wrongAnswers =
                    studyProgress.wrongAnswers + currentDateProgress.wrongAnswers
                mStudyProgressRepo.update(studyProgress)
            } else
                mStudyProgressRepo.create(studyProgress)
        }

    }

    override fun onDestroy() {
        this.mView = null
    }

}
