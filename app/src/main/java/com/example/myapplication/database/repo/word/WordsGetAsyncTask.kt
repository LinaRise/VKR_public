package com.example.myapplication.database.repo.word

import android.os.AsyncTask
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.setView.SetViewAdapter

class WordsGetAsyncTask (dbhelper: DBHelper, adapter: SetViewAdapter) : AsyncTask<Long, Unit, List<Word>>() {

    var mWordRepo: WordRepo = WordRepo(dbhelper)
    var mAdapter = adapter

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: List<Word>?) {
        super.onPostExecute(result)
        mAdapter.setData(result)
        mAdapter.notifyDataSetChanged()

    }


    override fun doInBackground(vararg p0: Long?): List<Word>? {
        Log.d("WordsGetAsyncTask class", "DoInBackground")
        return mWordRepo.getWordsOfSet(settId = p0[0]!!)
    }
}
