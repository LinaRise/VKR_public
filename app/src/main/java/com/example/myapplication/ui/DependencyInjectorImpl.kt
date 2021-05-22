package com.example.myapplication.ui

import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.studyProgress.StudyProgressRepo
import com.example.myapplication.database.repo.word.WordRepo

class DependencyInjectorImpl(var dbHelper: DBHelper) : DependencyInjector {
    override fun wordRepository(): WordRepo {
        return WordRepo(dbHelper)
    }

    override fun settRepository(): SettRepo {
        return SettRepo(dbHelper)
    }

    override fun languageRepository(): LanguageRepo {
       return LanguageRepo(dbHelper)
    }

    override fun studyProgressRepository(): StudyProgressRepo {
       return StudyProgressRepo(dbHelper)
    }

}
