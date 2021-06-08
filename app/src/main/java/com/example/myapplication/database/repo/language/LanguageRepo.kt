package com.example.myapplication.database.repo.language

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.database.repo.IRepository
import com.example.myapplication.entity.Language
import java.lang.Exception


class LanguageRepo(val dbhelper: DBHelper) : IRepository<Language> {
    private val TAG = "LanguageRepo"

    lateinit var db: SQLiteDatabase

    override fun create(entity: Language): Long {
        db = dbhelper.writableDatabase
        db.beginTransaction()
        var id = -1L
        try {
            val cv = ContentValues()
            cv.clear();
            cv.put(TablesAndColumns.LanguageEntry.COL_LANGUAGE_TITLE, entity.languageTitle)
            id = db.insertOrThrow(TablesAndColumns.LanguageEntry.TABLE_NAME, null, cv)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction()
            return id
        }
    }

    override fun update(entity: Language): Long {
        db = dbhelper.writableDatabase
        var updCount: Long = -1L
        val cv = ContentValues()
        cv.put(TablesAndColumns.LanguageEntry.COL_LANGUAGE_TITLE, entity.languageTitle)
        // обновляем по id
        db.beginTransaction()
        try {
            updCount = db.update(
                TablesAndColumns.LanguageEntry.TABLE_NAME, cv, "language_id = ?", arrayOf<String>(
                    entity.languageId.toString()
                )
            ).toLong()
            Log.d(TAG, "updated rows count = $updCount")
            db.setTransactionSuccessful()
        } catch (e: java.lang.Error) {
            print(e)
            Log.d(
                TAG, "Error while trying to update language at database with id = ${entity.languageId}"
            )
        } finally {
            db.endTransaction()
            return entity.languageId
        }
    }

    override fun delete(entity: Language): Long {
        db = dbhelper.writableDatabase
        db.beginTransaction()
        var delCount: Int = 0
        try {
            delCount = db.delete(
                TablesAndColumns.LanguageEntry.TABLE_NAME, "language_id = ?",
                arrayOf(entity.languageId.toString())
            )
            db.setTransactionSuccessful()
            Log.d(TAG, "deleted rows count = $delCount")
        } catch (e: java.lang.Error) {
            print(e)
            Log.d(TAG, "Error while trying to delete language from database with id = ${entity.languageId}")
        } finally {
            db.endTransaction()
            return delCount.toLong()
        }
    }

    override fun get(id: Long): Language? {
        db = dbhelper.readableDatabase
        db.beginTransaction()
        val cursor: Cursor? =
            db.rawQuery(
                "SELECT * FROM ${TablesAndColumns.LanguageEntry.TABLE_NAME} WHERE ${TablesAndColumns.LanguageEntry.TABLE_NAME}${BaseColumns._ID} = ?",
                arrayOf(id.toString())
            )
        var langauge: Language? = null
        if (cursor != null) {
            langauge = Language()
            if (cursor.moveToFirst()) {
                langauge.languageId = cursor.getLong(0)
                langauge.languageTitle = cursor.getString(1)
            }
            cursor.close()
        }
        db.setTransactionSuccessful()
        db.endTransaction()
        return langauge
    }

    fun getByTitle(title: String): Language? {
        db = dbhelper.readableDatabase
        db.beginTransaction()
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
            }
            cursor.close()
        }
        db.setTransactionSuccessful()
        db.endTransaction()
        return langauge
    }

    override fun getAll(): List<Language>? {
        TODO("Not yet implemented")
    }

}