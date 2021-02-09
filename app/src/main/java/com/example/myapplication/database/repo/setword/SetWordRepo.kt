package com.example.myapplication.database.repo.setword

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.database.repo.IRepository
import com.example.myapplication.entity.SetWord

class SetWordRepo(val dbhelper: DBHelper): IRepository<SetWord> {
    lateinit var db: SQLiteDatabase
    override fun create(entity: SetWord): Long {
        db = dbhelper.writableDatabase
        val cv = ContentValues()
        cv.clear();
        cv.put(TablesAndColumns.SettWordEntry.COL_SET_ID, entity.settId)
        cv.put(TablesAndColumns.SettWordEntry.COL_WORD_ID, entity.wordId)
        return db.insert(TablesAndColumns.SettWordEntry.TABLE_NAME, null, cv)
    }

    override fun update(entity: SetWord) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: SetWord): Long {
        TODO("Not yet implemented")
    }

    override fun get(id: Long): SetWord {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<SetWord>? {
        TODO("Not yet implemented")
    }


}