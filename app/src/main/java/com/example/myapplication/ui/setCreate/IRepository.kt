package com.example.myapplication.ui.setCreate

import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word


interface IRepository {
    fun getLanguageByTitle(title: String): Language?
    fun addSet(sett: Sett):Long
    fun addLanguage(title:String): Long
    fun addWord(word: Word):Long
    fun bindSetWord(wordId:Long, settId:Long):Long
}