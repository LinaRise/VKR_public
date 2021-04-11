package com.example.myapplication.database.repo.word


import android.os.AsyncTask
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Word

//import com.example.myapplication.entity.Word

class WordCreateAsyncTask(dbhelper: DBHelper) : AsyncTask<Word, Unit, Long>() {

    var mWordRepo: WordRepo = WordRepo(dbhelper)
//    var mSetWordRepo: SetWordRepo = SetWordRepo(dbhelper)

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: Long?) {
        super.onPostExecute(result)
    }

    override fun doInBackground(vararg p0: Word) :Long{
        val word = p0[0]
        return mWordRepo.create(word)
        }
    }


