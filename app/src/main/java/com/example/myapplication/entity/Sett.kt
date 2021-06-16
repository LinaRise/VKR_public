package com.example.myapplication.entity



data class Sett(// название
    var settId:Long = 0,
    var settTitle: String="" , // столица
    var languageInput_id: String="" ,
    var languageOutput_id: String="" ,
    var wordsAmount: Int=0,
    var hasAutoSuggest: Int=0
)