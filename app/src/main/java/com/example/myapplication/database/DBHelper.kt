package com.example.myapplication.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.myapplication.database.TablesAndColumns.LanguageEntry
import com.example.myapplication.database.TablesAndColumns.SettEntry
import com.example.myapplication.database.TablesAndColumns.WordEntry
import com.example.myapplication.database.TablesAndColumns.StudyProgressEntry


class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "wordsAndProgressDB.db"
        const val DATABASE_VERSION = 5


        private const val CREATE_TABLE_LANGUAGE =
            "CREATE TABLE ${LanguageEntry.TABLE_NAME} (${LanguageEntry.TABLE_NAME}${BaseColumns._ID} INTEGER PRIMARY KEY autoincrement, ${LanguageEntry.COL_LANGUAGE_TITLE} TEXT not null UNIQUE);"
        private const val CREATE_TABLE_STUDY_PROGRESS =
            "CREATE TABLE ${StudyProgressEntry.TABLE_NAME} (${StudyProgressEntry.COL_DATE} TEXT PRIMARY KEY, ${StudyProgressEntry.COL_RIGHT_ANSWERS} INTEGER DEFAULT 0, ${StudyProgressEntry.COL_WRONG_ANSWERS} INTEGER DEFAULT 0);"

        private const val CREATE_TABLE_WORD =
            "CREATE TABLE ${WordEntry.TABLE_NAME}  (${WordEntry.TABLE_NAME}${BaseColumns._ID} INTEGER PRIMARY KEY autoincrement, ${WordEntry.COL_ORIGINAL_WORD} text not null, ${WordEntry.COL_TRANSLATED_WORD} TEXT, " +
                    "${WordEntry.COL_SET_ID} INTEGER not null, " +
                    "${WordEntry.COL_RECALL_POINT} INTEGER not null DEFAULT 0, "+
                    "FOREIGN KEY (${WordEntry.COL_SET_ID}) REFERENCES ${SettEntry.TABLE_NAME} (${SettEntry.TABLE_NAME}${BaseColumns._ID}) ON DELETE CASCADE);"

        private const val CREATE_TABLE_SET =
            "CREATE TABLE ${SettEntry.TABLE_NAME} ( ${SettEntry.TABLE_NAME}${BaseColumns._ID}  INTEGER PRIMARY KEY autoincrement, " +
                    "${SettEntry.COL_SET_TITLE} TEXT not null, ${SettEntry.COL_LANGUAGE_INPUT_ID} INTEGER not null, ${SettEntry.COL_LANGUAGE_OUTPUT_ID} INTEGER not null, ${SettEntry.COL_WORDS_AMOUNT} INTEGER not null, " +
                    "${SettEntry.COL_AUTO_SUGGEST} INTEGER not null DEFAULT 0, "+
                    "FOREIGN KEY (${SettEntry.COL_LANGUAGE_INPUT_ID}) REFERENCES ${LanguageEntry.TABLE_NAME} (${LanguageEntry.TABLE_NAME}${BaseColumns._ID}), " +
                    "FOREIGN KEY (${SettEntry.COL_LANGUAGE_OUTPUT_ID}) REFERENCES ${LanguageEntry.TABLE_NAME} (${LanguageEntry.TABLE_NAME}${BaseColumns._ID})," +
                    "UNIQUE (${SettEntry.COL_SET_TITLE} ,${SettEntry.COL_LANGUAGE_INPUT_ID},${SettEntry.COL_LANGUAGE_OUTPUT_ID}) );"

       /* private const val CREATE_TABLE_SET_WORD =
            "CREATE TABLE ${SettWordEntry.TABLE_NAME} (${SettWordEntry.TABLE_NAME}${BaseColumns._ID} INTEGER PRIMARY KEY autoincrement, ${SettWordEntry.COL_SET_ID} INTEGER not null, ${SettWordEntry.COL_WORD_ID} INTEGER not null, " +
                    "FOREIGN KEY (${SettWordEntry.COL_SET_ID}) REFERENCES ${SettEntry.TABLE_NAME} (${SettEntry.TABLE_NAME}${BaseColumns._ID}) ON DELETE CASCADE, " +
                    "FOREIGN KEY (${SettWordEntry.COL_WORD_ID}) REFERENCES ${WordEntry.TABLE_NAME} (${WordEntry.TABLE_NAME}${BaseColumns._ID}) ON DELETE CASCADE );"
*/
       /* private const val INSERT_DEFAULT_LANGUAGES =
            "INSERT INTO ${LanguageEntry.TABLE_NAME} (${LanguageEntry.COL_LANGUAGE_TITLE},${LanguageEntry.COL_SUPPORTS_TRANSLATION}) VALUES ('English', 1), " +
                    "('Russian', 1), " +
                    "('French', 1), " +
                    "('Czech', 1), " +
                    "('German', 1);"
       *//* val values = ContentValues().apply {
            put(LanguageEntry.COL_LANGUAGE_TITLE, "English")
            put(LanguageEntry.COL_SUPPORTS_TRANSLATION, 1)
            put(LanguageEntry.COL_LANGUAGE_TITLE, "Russian")
            put(LanguageEntry.COL_SUPPORTS_TRANSLATION, 1)
            put(LanguageEntry.COL_LANGUAGE_TITLE, "French")
            put(LanguageEntry.COL_SUPPORTS_TRANSLATION, 1)
            put(LanguageEntry.COL_LANGUAGE_TITLE, "German")
            put(LanguageEntry.COL_SUPPORTS_TRANSLATION, 1)
            put(LanguageEntry.COL_LANGUAGE_TITLE, "Czech")
            put(LanguageEntry.COL_SUPPORTS_TRANSLATION, 1)
        }*/
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_LANGUAGE)
        db.execSQL(CREATE_TABLE_WORD)
        db.execSQL(CREATE_TABLE_SET)
        db.execSQL(CREATE_TABLE_STUDY_PROGRESS)
//        db.execSQL(CREATE_TABLE_SET_WORD)
//        db.execSQL(INSERT_DEFAULT_LANGUAGES)
    }
    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON")
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        database.execSQL("DROP TABLE IF EXISTS ${SettWordEntry.TABLE_NAME};")
        database.execSQL("DROP TABLE IF EXISTS ${SettEntry.TABLE_NAME}")
        database.execSQL("DROP TABLE IF EXISTS ${WordEntry.TABLE_NAME};")
        database.execSQL("DROP TABLE IF EXISTS ${LanguageEntry.TABLE_NAME};")
        database.execSQL("DROP TABLE IF EXISTS ${StudyProgressEntry.TABLE_NAME};")
        onCreate(database)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun clearDbAndRecreate() {
        clearDb()
        onCreate(writableDatabase)
    }

    fun clearDb() {
        writableDatabase.execSQL("DROP TABLE IF EXISTS ${SettEntry.TABLE_NAME}")
        writableDatabase.execSQL("DROP TABLE IF EXISTS ${WordEntry.TABLE_NAME};")
        writableDatabase.execSQL("DROP TABLE IF EXISTS ${LanguageEntry.TABLE_NAME};")
        writableDatabase.execSQL("DROP TABLE IF EXISTS ${StudyProgressEntry.TABLE_NAME};")
    }

}