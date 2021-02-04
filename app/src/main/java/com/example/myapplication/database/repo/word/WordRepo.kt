package com.example.myapplication.database.repo.word

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.database.repo.IRepository
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import java.lang.Error

class WordRepo(val dbhelper: DBHelper) : IRepository<Word> {
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

    fun getWordsOfSet(settId: Long):List<Word>? {
        db = dbhelper.readableDatabase
        val cursor: Cursor? =
            db.rawQuery(
                /*"SELECT w.* " +
                        "FROM  ${TablesAndColumns.WordEntry.TABLE_NAME} w " +
                        "LEFT OUTER JOIN ${TablesAndColumns.SettWordEntry.TABLE_NAME} sw " +
                        "ON w.${TablesAndColumns.WordEntry.TABLE_NAME}_id = sw.${TablesAndColumns.WordEntry.TABLE_NAME}_id " +
                        "LEFT OUTER JOIN ${TablesAndColumns.SettEntry.TABLE_NAME} s " +
                        "ON sw.sett_word_id = s.sett_id " +
                        "WHERE s.sett_id = ?",*/
                "SELECT w.* FROM  word w " +
                        "JOIN sett_word sw " +
                        "ON w.word_id = sw.word_id " +
                        "JOIN sett s " +
                        "ON sw.sett_id = s.sett_id ",
               null
            )
        Log.d("word class","")
        var word: Word?
        var wordList: ArrayList<Word> = ArrayList()
        if (cursor != null) {
            val colOriginalWord = cursor.getColumnIndex(TablesAndColumns.WordEntry.COL_ORIGINAL_WORD)
            val colTranslatedWord =
                cursor.getColumnIndex(TablesAndColumns.WordEntry.COL_TRANSLATED_WORD)

            Log.d("WordsGetAsyncTask","!!!")
            try {
                while (cursor.moveToNext()) {
                    word = Word()
                    word.wordId = cursor.getLong(0)
                    word.originalWord = cursor.getString(colOriginalWord)
                    word.translatedWord = cursor.getString(colTranslatedWord)
                    wordList.add(word)
                    Log.d("word class", word.originalWord)

                }

            } catch (e: Error) {
                print(e)
            } finally {
                cursor.close()
                db.close()
                return wordList
            }
        }
        return null
    }
}