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
    private val TAG = "WordRepo"
    lateinit var db: SQLiteDatabase
    override fun create(entity: Word): Long {
        db = dbhelper.writableDatabase
        var insertId: Long = -1L
        db.beginTransaction()
        try {
            val cv = ContentValues()
            cv.clear()
            cv.put(TablesAndColumns.WordEntry.COL_ORIGINAL_WORD, entity.originalWord)
            cv.put(TablesAndColumns.WordEntry.COL_TRANSLATED_WORD, entity.translatedWord)
            cv.put(TablesAndColumns.WordEntry.COL_SET_ID, entity.settId)
            cv.put(TablesAndColumns.WordEntry.COL_RECALL_POINT, entity.recallPoint)
            insertId = db.insertOrThrow(TablesAndColumns.WordEntry.TABLE_NAME, null, cv)
            db.setTransactionSuccessful();
        } catch (e: java.lang.Error) {
            Log.d(TAG, "Error while inserting to database")
        } finally {
            db.endTransaction()
            return insertId
        }
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
            Log.d(TAG, "updated rows count = $updCount")
            db.setTransactionSuccessful()
        } catch (e: java.lang.Error) {
            print(e)
            Log.d(
                TAG, "Error while trying to update word at database with id = ${entity.settId}"
            )
        } finally {
            db.endTransaction()
            return entity.settId
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
            print(e)
            Log.d(
                TAG, "Error while trying to delete word at database with id = ${entity.settId}"
            )
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
        db.beginTransaction()
        val wordList: ArrayList<Word> = ArrayList()
        try {
            val cursor: Cursor? =
                db.rawQuery(
                    "SELECT * FROM  word " +
                            "WHERE sett_id = ?",
                    arrayOf(settId.toString())
                )
            Log.d("word class", "")
            var word: Word?

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
                while (cursor.moveToNext()) {
                    word = Word()
                    word.wordId = cursor.getLong(0)
                    word.originalWord = cursor.getString(colOriginalWord)
                    word.translatedWord = cursor.getString(colTranslatedWord)
                    word.recallPoint = cursor.getInt(colRecallPoints)
                    word.settId = cursor.getLong(colSettId)
                    wordList.add(word)
                    Log.d("word class", word.originalWord)

                }
                cursor.close()
                db.setTransactionSuccessful()
            }
        } catch (e: Error) {
            print(e)
            print(e)
            Log.d(
                TAG, "Error while trying to get wordOfSets at database "
            )
        } finally {
            db.endTransaction()
            db.close()
            return wordList
        }
    }
}