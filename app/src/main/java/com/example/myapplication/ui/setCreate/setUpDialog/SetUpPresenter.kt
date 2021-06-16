package com.example.myapplication.ui.setCreate.setUpDialog

import android.os.Handler
import android.os.Looper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Sett
import com.example.myapplication.ui.DependencyInjector
import com.google.cloud.translate.Translate
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.HashMap

class SetUpPresenter(
    view: SetUpContract.View,
    dependencyInjector: DependencyInjector
) : SetUpContract.Presenter {
    private var mView: SetUpContract.View? = view
    private val mLanguageRepo: LanguageRepo = dependencyInjector.languageRepository()

    override fun onViewCreated(translate: Translate?) {
        if (translate != null) {
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            executor.execute {
                val languages = translate.listSupportedLanguages()
                val languagesSourceNames = languages.map { it.name }.toTypedArray()
                var languageCodeAndTitle: java.util.HashMap<String, String>
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

