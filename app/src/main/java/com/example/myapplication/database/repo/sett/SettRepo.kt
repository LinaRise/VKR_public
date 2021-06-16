package com.example.myapplication.database.repo.sett

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
    private val TAG = "SettRepo"

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
            Log.d(TAG, "Error while inserting to database");
        } finally {
            db.endTransaction()
            return id
        }
    }

    override fun update(entity: Sett): Long {
        db = dbhelper.writableDatabase
        var updCount = -1L
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
            updCount = db.update(
                TablesAndColumns.SettEntry.TABLE_NAME, cv, "sett_id = ?",
                arrayOf(entity.settId.toString())
            ).toLong()
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Error while trying to update sett at database with id = ${entity.settId}"
            )
        } finally {
            db.endTransaction()
            return entity.settId
        }
    }




    override fun get(id: Long): Sett? {
        db = dbhelper.readableDatabase

        db.beginTransaction()
        var sett: Sett? = null
        try {

            val cursor: Cursor? =
                db.rawQuery(
                    "SELECT * FROM ${TablesAndColumns.SettEntry.TABLE_NAME} WHERE ${TablesAndColumns.SettEntry.TABLE_NAME}${BaseColumns._ID} = ?",
                    arrayOf(id.toString())
                )
            if (cursor != null) {
                val colSetTitle = cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_SET_TITLE)
                val colLangInputId =
                    cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_LANGUAGE_INPUT_ID)
                val colLangOutputId =
                    cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_LANGUAGE_OUTPUT_ID)
                val colWordsAmount =
                    cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_WORDS_AMOUNT)
                val colAutoSuggest =
                    cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_AUTO_SUGGEST)
                sett = Sett()
                if (cursor.moveToFirst()) {
                    sett.settId = cursor.getLong(0)
                    sett.settTitle = cursor.getString(colSetTitle)
                    sett.languageInput_id = cursor.getString(colLangInputId)
                    sett.languageOutput_id = cursor.getString(colLangOutputId)
                    sett.wordsAmount = cursor.getInt(colWordsAmount)
                    sett.hasAutoSuggest = cursor.getInt(colAutoSuggest)

                }
                cursor.close()
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Error while trying to get sett from database with id = ${sett?.settId}"
            )
        } finally {
            db.endTransaction()
            db.close()
            return sett
        }


    }

    override fun getAll(): List<Sett>? {


        db = dbhelper.readableDatabase

        db.beginTransaction()
        var settList: ArrayList<Sett> = ArrayList()
        try {
            val cursor: Cursor? =
                db.rawQuery(
                    "SELECT * FROM ${TablesAndColumns.SettEntry.TABLE_NAME}",
                    null
                )


            var sett: Sett?

            if (cursor != null) {
                val colSetTitle = cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_SET_TITLE)
                val colLangInputId =
                    cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_LANGUAGE_INPUT_ID)
                val colLangOutputId =
                    cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_LANGUAGE_OUTPUT_ID)
                val colWordsAmount =
                    cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_WORDS_AMOUNT)
                val colAutoSuggest =
                    cursor.getColumnIndex(TablesAndColumns.SettEntry.COL_AUTO_SUGGEST)

                while (cursor.moveToNext()) {
                    sett = Sett()
                    sett.settId = cursor.getLong(0)
                    sett.settTitle = cursor.getString(colSetTitle)
                    sett.languageInput_id = cursor.getString(colLangInputId)
                    sett.languageOutput_id = cursor.getString(colLangOutputId)
                    sett.wordsAmount = cursor.getInt(colWordsAmount)
                    sett.hasAutoSuggest = cursor.getInt(colAutoSuggest)
                    settList.add(sett)
                }
                cursor.close()
            }
            db.setTransactionSuccessful()

        } catch (e: Error) {
            print(e)
            Log.d(
                TAG,
                "Error while trying to get all sets from database"
            );
        } finally {
            db.endTransaction()
            db.close()
            return settList
        }
    }


    override fun delete(entity: Sett): Long {
        db = dbhelper.writableDatabase
        db.beginTransaction()
        var delCount: Int = 0
        try {
            delCount = db.delete(
                TablesAndColumns.SettEntry.TABLE_NAME, "sett_id = ?",
                arrayOf(entity.settId.toString())
            )
            db.setTransactionSuccessful()
            Log.d(TAG, "deleted rows count = $delCount")
        } catch (e: java.lang.Error) {
            print(e)
            Log.d(TAG, "Error while trying to delete sett from database with id = ${entity.settId}")
        } finally {
            db.endTransaction()
            return delCount.toLong()
        }
    }
}