package com.example.myapplication.ui.setView.copyCardDialog

import android.os.Handler
import android.os.Looper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.DependencyInjector
import com.google.cloud.translate.Translate
import java.util.concurrent.Executors

class CopyCardPresenter(
    view: CopyCardContract.View,
    dependencyInjector: DependencyInjector
) : CopyCardContract.Presenter {
    private var mView: CopyCardContract.View? = view
    private var set: Sett = Sett()
    private val mSettRepo: SettRepo = dependencyInjector.settRepository()
    private val mLanguageRepo: LanguageRepo = dependencyInjector.languageRepository()
    private val mWordRepo: WordRepo = dependencyInjector.wordRepository()
    override fun onAddWordClicked(word: Word?, pickedSet: Sett) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var id = -1L
        executor.execute {
            id = mWordRepo.create(word!!)
            mSettRepo.update(pickedSet)
            handler.post {
                if (id != -1L)
                    mView?.showCopiedWordToSetToast()
            }
        }
    }


    override fun onDestroy() {
        this.mView = null
    }
}