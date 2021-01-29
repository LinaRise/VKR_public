package com.example.myapplication.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "sett",
    indices = [Index(value = ["set_title", "languageInput_id","languageOutput_id"], unique = true)]
)
data class Sett(// название
    @PrimaryKey
    var settId:Long = 0,

    @ColumnInfo(name = "set_title")
    var settTitle: String="" , // столица

    @ColumnInfo(name = "languageInput_id")
    var languageInput_id: Long=0 ,
    @ColumnInfo(name = "languageOutput_id")
    var languageOutput_id: Long=0 ,
    @ColumnInfo(name = "words_amount")
    var wordsAmount: Int=0
)