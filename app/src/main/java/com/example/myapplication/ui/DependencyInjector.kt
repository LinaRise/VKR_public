package com.example.myapplication.ui

import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.studyProgress.StudyProgressRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.StudyProgress

interface DependencyInjector {
    fun wordRepository(): WordRepo
    fun settRepository(): SettRepo
    fun languageRepository(): LanguageRepo
    fun studyProgressRepository(): StudyProgressRepo
}

