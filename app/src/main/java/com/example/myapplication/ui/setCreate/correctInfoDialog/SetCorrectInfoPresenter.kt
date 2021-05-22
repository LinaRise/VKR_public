package com.example.myapplication.ui.setCreate.correctInfoDialog

import android.os.Handler
import android.os.Looper
import com.example.myapplication.entity.Sett
import com.google.cloud.translate.Translate
import java.util.concurrent.Executors

class SetCorrectInfoPresenter(
    view: SetCorrectInfoContract.View,
) : SetCorrectInfoContract.Presenter {
    private var mView: SetCorrectInfoContract.View? = view
    private var set: Sett = Sett()
    override fun onViewCreated(translate: Translate?) {
        if (translate != null) {
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            executor.execute {
                val languages = translate.listSupportedLanguages()
                val languagesSourceNames = languages.map { it.name }.toTypedArray()
                val languageTitleAndCode = languages.map { it.name to it.code }.toMap()
                handler.post {
                    if (languages != null) {
                        mView?.setAvailableLanguagesInfo(languagesSourceNames,languageTitleAndCode)
                    }
                }
            }

        }
    }


    override fun onDestroy() {
        this.mView = null
    }
}