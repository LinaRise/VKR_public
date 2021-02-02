package com.example.myapplication.database.repo

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Sett
import java.lang.Error
import java.lang.Exception

class SettRepo(val dbhelper: DBHelper) : IRepository<Sett> {
    lateinit var db: SQLiteDatabase
    override fun create(entity: Sett): Long {
        db = dbhelper.writableDatabase
        db.beginTransaction()
        var id = -1L
        try {
            val cv = ContentValues()
            cv.clear()
            cv.put(TablesAndColumns.SettEntry.COL_SET_TITLE, entity.settTitle)
            cv.put(TablesAndColumns.SettEntry.COL_LANGUAGE_INPUT_ID, entity.languageInput_id)
            cv.put(TablesAndColumns.SettEntry.COL_LANGUAGE_OUTPUT_ID, entity.languageOutput_id)
            cv.put(TablesAndColumns.SettEntry.COL_WORDS_AMOUNT, entity.wordsAmount)
            id = db.insertOrThrow(TablesAndColumns.SettEntry.TABLE_NAME, null, cv)
            db.setTransactionSuccessful();
        } catch (e: Exception) {
            Log.d("LanguageRepo", "Error while trying to add post to database");
        } finally {
            db.endTransaction()
            return id
        }
    }

    override fun update(entity: Sett) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Sett) {
        TODO("Not yet implemented")
    }

    override fun get(id: Long): Sett {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Sett>? {


        db = dbhelper.readableDatabase


        val cursor: Cursor? =
            db.rawQuery(
                "SELECT * FROM ${TablesAndColumns.SettEntry.TABLE_NAME}",
                null
            )


        var sett: Sett?
        var settList: ArrayList<Sett> = ArrayList()
        if (cursor != null) {
            val colSetTitle = cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_SET_TITLE)
            val colLangInputId =
                cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_LANGUAGE_INPUT_ID)
            val colLangOutputId =
                cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_LANGUAGE_OUTPUT_ID)
            val colWordsAmount = cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_WORDS_AMOUNT)

            try {
                while (cursor.moveToNext()) {
                    sett = Sett()
                    sett.settId = cursor.getLong(0)
                    sett.settTitle = cursor.getString(colSetTitle)
                    sett.languageInput_id = cursor.getLong(colLangInputId)
                    sett.languageOutput_id = cursor.getLong(colLangOutputId)
                    sett.wordsAmount = cursor.getInt(colWordsAmount)
                    settList.add(sett)
                }

            } catch (e: Error) {
                print(e)
            } finally {
                cursor.close()
                db.close()
                return settList
            }
        }
        return null

    }
}