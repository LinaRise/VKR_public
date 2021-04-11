package com.example.myapplication.database.repo.word

import android.os.AsyncTask
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Word


class WordUpdateAsyncTask(dbhelper: DBHelper) : AsyncTask<Word, Unit, Long>() {

    var mWordRepo: WordRepo = WordRepo(dbhelper)

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: Long?) {
        super.onPostExecute(result)
    }

    override fun doInBackground(vararg p0: Word?): Long {
        val word = p0[0]
        if (word != null) {
            return mWordRepo.update(word)
        }

        return -1

    }


}