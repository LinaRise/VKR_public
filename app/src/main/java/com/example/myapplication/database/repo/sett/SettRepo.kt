package com.example.myapplication.database.repo.sett

import android.R.attr.name
import android.R.id
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.database.repo.IRepository
import com.example.myapplication.entity.Sett


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
            cv.put(TablesAndColumns.SettEntry.COL_AUTO_SUGGEST, entity.hasAutoSuggest)
            id = db.insertOrThrow(TablesAndColumns.SettEntry.TABLE_NAME, null, cv)
            db.setTransactionSuccessful();
        } catch (e: Exception) {
            Log.d("SettRepo", "Error while trying to add sett to database");
        } finally {
            db.endTransaction()
            return id
        }
    }

    override fun update(entity: Sett) {
        db = dbhelper.writableDatabase
        db.beginTransaction()
        try {
            val cv = ContentValues()
            cv.clear()
            cv.put(TablesAndColumns.SettEntry.COL_SET_TITLE, entity.settTitle)
            cv.put(TablesAndColumns.SettEntry.COL_LANGUAGE_INPUT_ID, entity.languageInput_id)
            cv.put(TablesAndColumns.SettEntry.COL_LANGUAGE_OUTPUT_ID, entity.languageOutput_id)
            cv.put(TablesAndColumns.SettEntry.COL_WORDS_AMOUNT, entity.wordsAmount)
            cv.put(TablesAndColumns.SettEntry.COL_AUTO_SUGGEST, entity.hasAutoSuggest)
        // обновляем по id
        val updCount = db.update(TablesAndColumns.SettEntry.TABLE_NAME, cv, "sett_id = ?",
            arrayOf(entity.settId.toString()))
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d("SettRepo", "Error while trying to update sett at database with id = ${entity.settId}");
        } finally {
            db.endTransaction()

        }
    }

    override fun delete(entity: Sett): Long {
        TODO("Not yet implemented")
    }

    override fun get(id: Long): Sett? {
        db = dbhelper.readableDatabase
        val cursor: Cursor? =
            db.rawQuery(
                "SELECT * FROM ${TablesAndColumns.SettEntry.TABLE_NAME} WHERE ${TablesAndColumns.SettEntry.TABLE_NAME}${BaseColumns._ID} = ?",
                arrayOf(id.toString())
            )

        var sett: Sett? = null
        if (cursor != null) {
            val colSetTitle = cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_SET_TITLE)
            val colLangInputId =
                cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_LANGUAGE_INPUT_ID)
            val colLangOutputId =
                cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_LANGUAGE_OUTPUT_ID)
            val colWordsAmount = cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_WORDS_AMOUNT)
            val colAutoSuggest = cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_AUTO_SUGGEST)
            sett = Sett()
            if (cursor.moveToFirst()) {
                sett.settId = cursor.getLong(0)
                sett.settTitle = cursor.getString(colSetTitle)
                sett.languageInput_id = cursor.getLong(colLangInputId)
                sett.languageOutput_id = cursor.getLong(colLangOutputId)
                sett.wordsAmount = cursor.getInt(colWordsAmount)
                sett.hasAutoSuggest = cursor.getInt(colAutoSuggest)

            }
            cursor.close()
        }

        db.close()
        return sett
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
            val colAutoSuggest = cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_AUTO_SUGGEST)

            try {
                while (cursor.moveToNext()) {
                    sett = Sett()
                    sett.settId = cursor.getLong(0)
                    sett.settTitle = cursor.getString(colSetTitle)
                    sett.languageInput_id = cursor.getLong(colLangInputId)
                    sett.languageOutput_id = cursor.getLong(colLangOutputId)
                    sett.wordsAmount = cursor.getInt(colWordsAmount)
                    sett.hasAutoSuggest = cursor.getInt(colAutoSuggest)
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