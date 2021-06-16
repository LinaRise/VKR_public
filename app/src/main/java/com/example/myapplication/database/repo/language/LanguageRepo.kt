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
import com.example.myapplication.entity.Sett
import java.lang.Exception
import java.util.*


class LanguageRepo(val dbhelper: DBHelper) : IRepository<Language> {
    private val TAG = "LanguageRepo"

    lateinit var db: SQLiteDatabase

    override fun create(entity: Language): String {
        db = dbhelper.writableDatabase
        db.beginTransaction()
        var id = ""
        try {
            val cv = ContentValues()
            cv.clear()
            if (entity.languageId!="") {
                cv.put(
                    "${TablesAndColumns.LanguageEntry.TABLE_NAME}${BaseColumns._ID}",
                    entity.languageId
                )

                db.insertOrThrow(TablesAndColumns.LanguageEntry.TABLE_NAME, null, cv).toString()

                cv.clear()
                cv.put(TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_TITLE, entity.languageTitle)
                cv.put(TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_IS_DEFAULT, 1)
                cv.put(TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_CODE, Locale.getDefault().language)
                cv.put(TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGE_CODE, entity.languageId)
                db.insertOrThrow(TablesAndColumns.LanguageTranslationEntry.TABLE_NAME, null, cv)
                    .toString()
            }
            else {
                cv.put(
                    "${TablesAndColumns.LanguageEntry.TABLE_NAME}${BaseColumns._ID}",
                    entity.languageTitle
                )
                db.insertOrThrow(TablesAndColumns.LanguageEntry.TABLE_NAME, null, cv).toString()
                cv.clear()
                cv.put(TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_TITLE, entity.languageTitle)
                cv.put(TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_IS_DEFAULT, 1)
                cv.put(TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_CODE, Locale.getDefault().language)
                cv.put(TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGE_CODE, entity.languageTitle)
                db.insertOrThrow(TablesAndColumns.LanguageTranslationEntry.TABLE_NAME, null, cv)
            }

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction()
            return entity.languageId
        }
    }

    override fun update(entity: Language): String {
        db = dbhelper.writableDatabase
        var updCount: Long = -1L
        val cv = ContentValues()
//        cv.put(TablesAndColumns.LanguageEntry.COL_LANGUAGE_TITLE, entity.languageTitle)
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
                TAG,
                "Error while trying to update language at database with id = ${entity.languageId}"
            )
        } finally {
            db.endTransaction()
            return entity.languageId
        }
    }


    fun getLanguageTitleLocale(languageCode: String, languageTRCode: String): String {
        db = dbhelper.readableDatabase

        db.beginTransaction()
        var title = ""
        try {
            val cursor: Cursor? =
                db.rawQuery(
                    "SELECT " +
                            "coalesce (tr.${TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_TITLE}, def.${TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_TITLE}) " +
                            "FROM ${TablesAndColumns.LanguageEntry.TABLE_NAME} l " +
                            "LEFT OUTER JOIN ${TablesAndColumns.LanguageTranslationEntry.TABLE_NAME} tr " +
                            "ON l.${TablesAndColumns.LanguageEntry.TABLE_NAME}${BaseColumns._ID} = tr.${TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGE_CODE} " +
                            "AND tr.${TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_CODE} = ? " +
                            "LEFt OUTER JOIN ${TablesAndColumns.LanguageTranslationEntry.TABLE_NAME} def " +
                            "ON l.${TablesAndColumns.LanguageEntry.TABLE_NAME}${BaseColumns._ID} = def.${TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGE_CODE} AND def.${TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_IS_DEFAULT} = 1 " +
                            "WHERE l.${TablesAndColumns.LanguageEntry.TABLE_NAME}${BaseColumns._ID} = ?",
                    arrayOf(languageTRCode, languageCode)
                )
            if (cursor != null) {
                val colLangiageTitle =
                    cursor.getColumnIndex(TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_TITLE)
                if (cursor.moveToFirst()) {
                    title = cursor.getString(0)
                }
                cursor.close()
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Error while trying to get language translation $languageTRCode from database with id = $languageCode"
            )
        } finally {
            db.endTransaction()
            return title
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
            Log.d(
                TAG,
                "Error while trying to delete language from database with id = ${entity.languageId}"
            )
        } finally {
            db.endTransaction()
            return delCount.toLong()
        }
    }

     fun get(id: String): Language? {
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
                langauge.languageId = cursor.getString(0)
            }
            cursor.close()
        }
        db.setTransactionSuccessful()
        db.endTransaction()
        return langauge
    }

    fun getByLocaleTitle(title: String): Language? {
        db = dbhelper.readableDatabase
        db.beginTransaction()
        val cursor: Cursor? =
            db.rawQuery(
                "SELECT coalesce(${TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGE_CODE}, '') AS code FROM " +
                        "${TablesAndColumns.LanguageTranslationEntry.TABLE_NAME} WHERE ${TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_TITLE} = ? ",
                arrayOf(title)
            )
        var langauge: Language? = null
        if (cursor != null) {
            langauge = Language()
            if (cursor.moveToFirst()) {
                langauge.languageId = cursor.getString(0)
                langauge.languageTitle = title
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
                "SELECT coalesce(${TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGE_CODE}, ''), coalesce(${TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_TITLE}, '') FROM " +
                        "${TablesAndColumns.LanguageTranslationEntry.TABLE_NAME}  WHERE ${TablesAndColumns.LanguageTranslationEntry.COL_LANGUAGETR_TITLE} = ?",
                arrayOf(title)
            )
        /* db.rawQuery(
             "SELECT * FROM ${TablesAndColumns.LanguageEntry.TABLE_NAME} WHERE ${TablesAndColumns.LanguageEntry.COL_LANGUAGE_TITLE} = '$title'",
             null
         )*/
        var langauge: Language? = null
        if (cursor != null) {
            langauge = Language()
            if (cursor.moveToFirst()) {
                langauge.languageId = cursor.getString(0)
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

    override fun get(id: Long): Language? {
        TODO("Not yet implemented")
    }

}