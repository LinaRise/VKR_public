package com.example.myapplication.ui.setCreate

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word


class SetCreateRepo(val dbhelper: DBHelper) : IRepository {
    lateinit var db: SQLiteDatabase
    override fun getLanguageByTitle(title: String): Language? {
        db = dbhelper.readableDatabase
        val cursor: Cursor? =
            db.rawQuery(
                "SELECT * FROM ${TablesAndColumns.LanguageEntry.TABLE_NAME} WHERE ${TablesAndColumns.LanguageEntry.COL_LANGUAGE_TITLE} = '$title'",
                null
            )
        var langauge: Language? = null
        if (cursor != null) {
            langauge = Language()
            if (cursor.moveToFirst()) {
                langauge.languageId = cursor.getLong(0)
                langauge.languageTitle = cursor.getString(1)
                langauge.supports_translation = cursor.getLong(2)
            }
            cursor.close()
        }

        db.close()
        return langauge
    }

    override fun addSet(sett: Sett): Long {
        db = dbhelper.writableDatabase
        val cv = ContentValues()
        cv.clear();
        cv.put(TablesAndColumns.SettEntry.COL_SET_TITLE, sett.settTitle)
        cv.put(TablesAndColumns.SettEntry.COL_LANGUAGE_INPUT_ID, sett.languageInput_id)
        cv.put(TablesAndColumns.SettEntry.COL_LANGUAGE_OUTPUT_ID, sett.languageOutput_id)
        cv.put(TablesAndColumns.SettEntry.COL_WORDS_AMOUNT, sett.wordsAmount)

        return db.insert(TablesAndColumns.SettEntry.TABLE_NAME, null, cv)
    }

    override fun addLanguage(title: String): Long {
        db = dbhelper.writableDatabase
        val cv = ContentValues()
        cv.clear();
        cv.put(TablesAndColumns.LanguageEntry.COL_LANGUAGE_TITLE, title)
        cv.put(TablesAndColumns.LanguageEntry.COL_SUPPORTS_TRANSLATION, 0)
        return db.insert(TablesAndColumns.LanguageEntry.TABLE_NAME, null, cv);
    }

    override fun addWord(word: Word): Long {
        db = dbhelper.writableDatabase
        val cv = ContentValues()
        cv.clear();
        cv.put(TablesAndColumns.WordEntry.COL_ORIGINAL_WORD, word.originalWord)
        cv.put(TablesAndColumns.WordEntry.COL_TRANSLATED_WORD, word.translatedWord)
        return db.insert(TablesAndColumns.WordEntry.TABLE_NAME, null, cv)

    }

    override fun bindSetWord(wordId: Long, settId: Long):Long {
        db = dbhelper.writableDatabase
        val cv = ContentValues()
        cv.clear()
        cv.put(TablesAndColumns.SettWordEntry.COL_WORD_ID, wordId)
         cv.put(TablesAndColumns.SettWordEntry.COL_SET_ID, settId)
        return db.insert(TablesAndColumns.SettWordEntry.TABLE_NAME, null, cv)
    }
}