package com.example.myapplication.entity

import java.time.LocalDate
import java.util.*


data class StudyProgress(// название
    var date: LocalDate = LocalDate.parse("2018-12-12"),
    var rightAnswers: Int=0, // столица
    var wrongAnswers: Int=0,
)