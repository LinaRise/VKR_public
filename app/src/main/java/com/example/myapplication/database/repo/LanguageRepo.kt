package com.example.myapplication.database.repo

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.entity.Language


class LanguageRepo(val dbhelper: DBHelper):IRepository<Language> {
    lateinit var db: SQLiteDatabase
    override fun create(entity: Language): Long {
        db = dbhelper.writableDatabase
        val cv = ContentValues()
        cv.clear();
        cv.put(TablesAndColumns.LanguageEntry.COL_LANGUAGE_TITLE, entity.languageTitle)
        cv.put(TablesAndColumns.LanguageEntry.COL_SUPPORTS_TRANSLATION, 0)
        return db.insert(TablesAndColumns.LanguageEntry.TABLE_NAME, null, cv)
    }

    override fun update(entity: Language) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Language) {
        TODO("Not yet implemented")
    }

    override fun get(id: Long) {
        TODO("Not yet implemented")
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