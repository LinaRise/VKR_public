package com.example.myapplication.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE


data class Sett(// название
    var settId:Long = 0,

    var settTitle: String="" , // столица

    var languageInput_id: Long=0 ,
    var languageOutput_id: Long=0 ,
    var wordsAmount: Int=0
)