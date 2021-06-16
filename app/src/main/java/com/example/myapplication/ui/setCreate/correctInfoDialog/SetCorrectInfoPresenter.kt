package com.example.myapplication.ui.setCreate.correctInfoDialog

import android.os.Handler
import android.os.Looper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.entity.Sett
import com.example.myapplication.ui.DependencyInjector
import com.google.cloud.translate.Translate
import java.util.*
import java.util.concurrent.Executors

class SetCorrectInfoPresenter(
    view: SetCorrectInfoContract.View,
    dependencyInjector: DependencyInjector

) : SetCorrectInfoContract.Presenter {
    private var mView: SetCorrectInfoContract.View? = view
    private val mLanguageRepo: LanguageRepo = dependencyInjector.languageRepository()

    override fun onViewCreated(translate: Translate?) {
        if (translate != null) {
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            executor.execute {
                val languages = translate.listSupportedLanguages()
                var languageCodeAndTitle: HashMap<String, String>
                handler.post {
                    if (languages != null) {
                        languageCodeAndTitle = languages.map {
                            it.code to
                                    mLanguageRepo.getLanguageTitleLocale(
                                        it.code,
                                        Locale.getDefault().language
                                    )
                        }.toMap() as HashMap<String, String>

                        mView?.setAvailableLanguagesInfo(
                            languageCodeAndTitle
                        )
                    }
                }
            }

        }
    }


    override fun onDestroy() {
        this.mView = null
    }
}