package com.example.myapplication.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class Language(

    var languageId: Long = 0,
    var languageTitle: String = "",
    var supports_translation: Long = 0,
)