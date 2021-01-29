package com.example.myapplication.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word")
data class Word (
    @PrimaryKey(autoGenerate = true)
    var  wordId: Long=0,
    @ColumnInfo(name = "original_word")
    var originalWord:String="",
    @ColumnInfo(name = "translated_word")
    var translatedWord:String=""

)