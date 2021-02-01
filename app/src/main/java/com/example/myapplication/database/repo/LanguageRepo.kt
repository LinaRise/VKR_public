package com.example.myapplication.database.repo

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.entity.Language
import java.lang.Exception


class LanguageRepo(val dbhelper: DBHelper) : IRepository<Language> {
    lateinit var db: SQLiteDatabase
    override fun create(entity: Language): Long {
        db = dbhelper.writableDatabase
        db.setTransactionSuccessful()
        var id = -1L
        try {
            val cv = ContentValues()
            cv.clear();
            cv.put(TablesAndColumns.LanguageEntry.COL_LANGUAGE_TITLE, entity.languageTitle)
            cv.put(TablesAndColumns.LanguageEntry.COL_SUPPORTS_TRANSLATION, 0)
            var id = db.insertOrThrow(TablesAndColumns.LanguageEntry.TABLE_NAME, null, cv)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d("LanguageRepo", "Error while trying to add post to database");
        } finally {
            db.endTransaction()
            return id

        }
    }

    override fun update(entity: Language) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Language) {
        TODO("Not yet implemented")
    }

    override fun get(id: Long): Language? {
        db = dbhelper.readableDatabase

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
                langauge.supports_translation = cursor.getLong(2)
            }
            cursor.close()
        }

        db.close()
        return langauge
    }

    fun getByTitle(title: String): Language? {
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

    override fun getAll(): List<Language>? {
        TODO("Not yet implemented")
    }

}