package com.example.myapplication.database.repo.word

import android.os.AsyncTask
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Word

class WordDeleteAsyncTask(dbhelper: DBHelper) : AsyncTask<Word, Unit, Unit>() {

    var mWordRepo: WordRepo = WordRepo(dbhelper)

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
//        Log.d("DEBUG_TAG", "Language inserted with id $result")

    }


    override fun doInBackground(vararg p0: Word?) {
        val word = p0[0] as Word
        val wordId = mWordRepo.delete(word)
//        mSetWordRepo.delete(SetWord(settId = settId, wordId = wordId))
    }
}


