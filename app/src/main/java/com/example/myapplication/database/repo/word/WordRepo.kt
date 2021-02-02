package com.example.myapplication.database.repo.word

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.database.repo.IRepository
import com.example.myapplication.entity.Word

class WordRepo(val dbhelper: DBHelper): IRepository<Word> {
    lateinit var db: SQLiteDatabase
    override fun create(entity: Word): Long {
        db = dbhelper.writableDatabase
        val cv = ContentValues()
        cv.clear();
        cv.put(TablesAndColumns.WordEntry.COL_ORIGINAL_WORD, entity.originalWord)
        cv.put(TablesAndColumns.WordEntry.COL_TRANSLATED_WORD, entity.translatedWord)
        return db.insert(TablesAndColumns.WordEntry.TABLE_NAME, null, cv)
    }

    override fun update(entity: Word) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Word) {
        TODO("Not yet implemented")
    }

    override fun get(id: Long): Word {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Word>? {
        TODO("Not yet implemented")
    }
}