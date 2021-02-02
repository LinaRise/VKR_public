package com.example.myapplication.database

import android.content.Context
import android.os.AsyncTask

class BackgroundTask(context: Context) : AsyncTask<String, Unit, Unit>() {
    var context: Context = context

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
    }

    override fun onProgressUpdate(vararg values: Unit?) {
        super.onProgressUpdate(*values)
    }
}