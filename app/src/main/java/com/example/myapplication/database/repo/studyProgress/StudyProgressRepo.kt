package com.example.myapplication.database.repo.studyProgress


import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.TablesAndColumns
import com.example.myapplication.database.repo.IRepository
import com.example.myapplication.entity.StudyProgress
import java.time.LocalDate
import kotlin.collections.ArrayList


class StudyProgressRepo(val dbhelper: DBHelper) : IRepository<StudyProgress> {
    private val TAG = "StudyProgressRepo"

    lateinit var db: SQLiteDatabase
    override fun create(entity: StudyProgress): Long {
        db = dbhelper.writableDatabase
        db.beginTransaction()
        var id = -1L
        try {
            val cv = ContentValues()
            cv.clear()
            cv.put(TablesAndColumns.StudyProgressEntry.COL_DATE, entity.date.toString())
            cv.put(TablesAndColumns.StudyProgressEntry.COL_RIGHT_ANSWERS, entity.rightAnswers)
            cv.put(TablesAndColumns.StudyProgressEntry.COL_WRONG_ANSWERS, entity.wrongAnswers)
            id = db.insertOrThrow(TablesAndColumns.StudyProgressEntry.TABLE_NAME, null, cv)
            db.setTransactionSuccessful();
        } catch (e: Exception) {
            Log.e(TAG, "Error while inserting to database");
        } finally {
            db.endTransaction()
            return id
        }
    }

    override fun update(entity: StudyProgress): Long {
        db = dbhelper.writableDatabase
        var updCount = -1L
        db.beginTransaction()
        try {
            val cv = ContentValues()
            cv.clear()
            cv.put(TablesAndColumns.StudyProgressEntry.COL_DATE, entity.date.toString())
            cv.put(TablesAndColumns.StudyProgressEntry.COL_RIGHT_ANSWERS, entity.rightAnswers)
            cv.put(TablesAndColumns.StudyProgressEntry.COL_WRONG_ANSWERS, entity.wrongAnswers)
            // обновляем по id
            updCount = db.update(
                TablesAndColumns.StudyProgressEntry.TABLE_NAME, cv, "study_progress_date = ?",
                arrayOf(entity.date.toString())
            ).toLong()
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Error while trying to update sett at database with id = ${entity.date}"
            )
        } finally {
            db.endTransaction()
            return -1
        }
    }


    fun get(date: LocalDate): StudyProgress {
        db = dbhelper.readableDatabase
        db.beginTransaction()
        /*Log.d(
            TAG,
            date.toString()
        )*/
        val studyProgress = StudyProgress()
        try {
            val cursor: Cursor? =
                db.rawQuery(
                    "SELECT * FROM ${TablesAndColumns.StudyProgressEntry.TABLE_NAME} WHERE ${TablesAndColumns.StudyProgressEntry.COL_DATE} = ?",
                    arrayOf(date.toString())
                )

            if (cursor != null) {
                val colRightAnswers =
                    cursor.getColumnIndex(TablesAndColumns.StudyProgressEntry.COL_RIGHT_ANSWERS)
                val colWrongAnswers =
                    cursor.getColumnIndex(TablesAndColumns.StudyProgressEntry.COL_WRONG_ANSWERS)
                if (cursor.moveToFirst()) {
                   /* studyProgress.date =LocalDate.parse(cursor.getString(0))
                    studyProgress.rightAnswers = cursor.getInt(1)
                    studyProgress.wrongAnswers = cursor.getInt(2)*/
                    studyProgress.date = LocalDate.parse(cursor.getString(0))
                    studyProgress.rightAnswers = cursor.getInt(colRightAnswers)
                    studyProgress.wrongAnswers = cursor.getInt(colWrongAnswers)
                }
                cursor.close()
            }
            db.setTransactionSuccessful()

        } catch (e: Exception) {
            Log.e(
                TAG,
                "Error while trying to get progress from database with id = ${studyProgress?.date}"
            )
        } finally {
            db.endTransaction()
            return studyProgress
        }
    }

    override fun getAll(): List<StudyProgress> {
        db = dbhelper.readableDatabase
        db.beginTransaction()
        val settList: ArrayList<StudyProgress> = ArrayList()
        try {
            val cursor: Cursor? =
                db.rawQuery(
                    "SELECT * FROM ${TablesAndColumns.StudyProgressEntry.TABLE_NAME}",
                    null
                )
            if (cursor != null) {
                val colDate = cursor.getColumnIndex(TablesAndColumns.StudyProgressEntry.COL_DATE)
                val colRightAnswers =
                    cursor.getColumnIndex(TablesAndColumns.StudyProgressEntry.COL_RIGHT_ANSWERS)
                val colWrongAnswers =
                    cursor.getColumnIndex(TablesAndColumns.StudyProgressEntry.COL_WRONG_ANSWERS)
                while (cursor.moveToNext()) {
                    val studyProgress = StudyProgress()
                    studyProgress.date = LocalDate.parse(cursor.getString(colDate))
                    studyProgress.rightAnswers = cursor.getInt(colRightAnswers)
                    studyProgress.wrongAnswers = cursor.getInt(colWrongAnswers)
                    settList.add(studyProgress)
                }
                cursor.close()
            }
            db.setTransactionSuccessful()

        } catch (e: Error) {
            print(e)
            Log.d(
                TAG,
                "Error while trying to get all study progress from database"
            );
        } finally {
            db.endTransaction()
            return settList
        }
    }


    override fun delete(entity: StudyProgress): Long {
        db = dbhelper.writableDatabase
        db.beginTransaction()
        var delCount: Int = 0
        try {
            delCount = db.delete(
                TablesAndColumns.StudyProgressEntry.TABLE_NAME, "study_progress_date = ?",
                arrayOf(entity.date.toString())
            )
            db.setTransactionSuccessful()
            Log.d(TAG, "deleted rows count = $delCount")
        } catch (e: java.lang.Error) {
            print(e)
            Log.d(TAG, "Error while trying to delete sett from database with id = ${entity.date}")
        } finally {
            db.endTransaction()
            return delCount.toLong()
        }
    }

    override fun get(id: Long): StudyProgress? {
        TODO("Not yet implemented")
    }

}