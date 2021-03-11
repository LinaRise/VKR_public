package com.example.myapplication.database.repo.word

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.database.repo.IRepository
import com.example.myapplication.entity.Word


class WordRepo(val dbhelper: DBHelper) : IRepository<Word> {
    lateinit var db: SQLiteDatabase
    override fun create(entity: Word): Long {
        db = dbhelper.writableDatabase
        val cv = ContentValues()
        cv.clear();
        cv.put(TablesAndColumns.WordEntry.COL_ORIGINAL_WORD, entity.originalWord)
        cv.put(TablesAndColumns.WordEntry.COL_TRANSLATED_WORD, entity.translatedWord)
        cv.put(TablesAndColumns.WordEntry.COL_SET_ID, entity.settId)
        cv.put(TablesAndColumns.WordEntry.COL_RECALL_POINT, entity.recallPoint)
        return db.insert(TablesAndColumns.WordEntry.TABLE_NAME, null, cv)
    }

    override fun update(entity: Word): Long {
        db = dbhelper.writableDatabase
        var updCount: Long = -1L
        val cv = ContentValues()
        cv.put(TablesAndColumns.WordEntry.COL_ORIGINAL_WORD, entity.originalWord)
        cv.put(TablesAndColumns.WordEntry.COL_TRANSLATED_WORD, entity.translatedWord)
        cv.put(TablesAndColumns.WordEntry.COL_SET_ID, entity.settId)
        cv.put(TablesAndColumns.WordEntry.COL_RECALL_POINT, entity.recallPoint)
        // обновляем по id
        db.beginTransaction()
        try {
            updCount = db.update(
                TablesAndColumns.WordEntry.TABLE_NAME, cv, "word_id = ?", arrayOf<String>(
                    entity.wordId.toString()
                )
            ).toLong()
            Log.d("Word Repo ", "updated rows count = $updCount")
            db.setTransactionSuccessful()
        } catch (e: java.lang.Error) {

        } finally {
            db.endTransaction()
            return updCount
        }


    }

    override fun delete(entity: Word): Long {
        db = dbhelper.writableDatabase
        db.beginTransaction()
        var delCount: Int = 0
        try {
            delCount = db.delete(
                TablesAndColumns.WordEntry.TABLE_NAME, "word_id = ?",
                arrayOf(entity.wordId.toString())

            )
            db.setTransactionSuccessful()
            Log.d("Word Repo", "deleted rows count = $delCount")
        } catch (e: java.lang.Error) {

        } finally {
            db.endTransaction()
            return delCount.toLong()
        }

    }

    override fun get(id: Long): Word {
        TODO("Not yet implemented")
    }


    override fun getAll(): List<Word>? {
        TODO("Not yet implemented")
    }

    fun getWordsOfSet(settId: Long): List<Word>? {
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
                /*"SELECT w.* FROM  word w " +
                        "JOIN sett_word sw " +
                        "ON w.word_id = sw.word_id " +
                        "JOIN sett s " +
                        "ON sw.sett_id = s.sett_id " +
                        "WHERE s.sett_id = ?",*/
                "SELECT * FROM  word " +
                        "WHERE sett_id = ?",
                        arrayOf (settId.toString())
            )
        Log.d("word class", "")
        var word: Word?
        var wordList: ArrayList<Word> = ArrayList()
        if (cursor != null) {
            val colOriginalWord =
                cursor.getColumnIndex(TablesAndColumns.WordEntry.COL_ORIGINAL_WORD)
            val colTranslatedWord =
                cursor.getColumnIndex(TablesAndColumns.WordEntry.COL_TRANSLATED_WORD)
            val colSettId =
                cursor.getColumnIndex(TablesAndColumns.WordEntry.COL_SET_ID)
            val colRecallPoints =
                cursor.getColumnIndex(TablesAndColumns.WordEntry.COL_RECALL_POINT)
            Log.d("WordsGetAsyncTask", "!!!")
            try {
                while (cursor.moveToNext()) {
                    word = Word()
                    word.wordId = cursor.getLong(0)
                    word.originalWord = cursor.getString(colOriginalWord)
                    word.translatedWord = cursor.getString(colTranslatedWord)
                    word.recallPoint = cursor.getLong(colRecallPoints)
                    word.settId = cursor.getLong(colSettId)
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