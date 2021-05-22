package com.example.myapplication.ui.setCreate.setUpDialog

import com.example.myapplication.entity.Sett
import com.google.cloud.translate.Translate

class SetUpPresenter(
    view: SetUpContract.View,
) : SetUpContract.Presenter {
    private var mView: SetUpContract.View? = view
    private var set: Sett = Sett()
    override fun onViewCreated(translate: Translate?) {
        if (translate != null) {
            val languages = translate.listSupportedLanguages()
           val languagesSourceNames = languages.map { it.name }.toTypedArray()
            val languageTitleAndCode = languages.map { it.name to it.code }.toMap()
        }

    }


    override fun onDestroy() {
        this.mView = null
    }
}