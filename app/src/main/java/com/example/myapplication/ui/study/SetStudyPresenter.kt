package com.example.myapplication.ui.study

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.studyProgress.StudyProgressRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.StudyProgress
import com.example.myapplication.entity.Word
import java.time.LocalDate
import java.util.concurrent.Executors

class SetStudyPresenter(
    view: ISetStudyView,
    dbhelper: DBHelper
) {
    private var mView: ISetStudyView = view

    var mWordRepo: WordRepo = WordRepo(dbhelper)
    var mStudyProgressRepo: StudyProgressRepo = StudyProgressRepo(dbhelper)
    fun updateWordsPoints(list: List<Word>) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            for (word in list) {
                mWordRepo.update(word)
            }

        }
    }

    fun updateStudyProgress(studyProgress: StudyProgress) {
        val executor = Executors.newSingleThreadExecutor()
        Log.d("updateStudyProgress", studyProgress.date.toString())
        executor.execute {
            val currentDateProgress = mStudyProgressRepo.get(studyProgress.date)
            if ( currentDateProgress.date != LocalDate.parse("2018-12-12")) {
                studyProgress.rightAnswers =
                    studyProgress.rightAnswers + currentDateProgress.rightAnswers
                studyProgress.wrongAnswers =
                    studyProgress.wrongAnswers + currentDateProgress.wrongAnswers
                mStudyProgressRepo.update(studyProgress)
            } else
                mStudyProgressRepo.create(studyProgress)
        }

    }

}
