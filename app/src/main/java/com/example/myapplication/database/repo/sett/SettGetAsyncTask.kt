package com.example.myapplication.database.repo.sett

import android.os.AsyncTask
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Sett
import java.lang.ref.WeakReference


/*
class SettGetAsyncTask(dbhelper: DBHelper, taskListener: TaskListener) :
    AsyncTask<Long, Unit, Sett>() {
    var mTaskListenerRef: WeakReference<TaskListener>? = WeakReference(taskListener)
    var sett: Sett? = null
    var mSettRepo: SettRepo = SettRepo(dbhelper)
    override fun doInBackground(vararg p0: Long?): Sett? {
        val settId = p0[0]
        if (settId != null) {
            sett = mSettRepo.get(settId)
        }
        return sett

    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: Sett?) {
        super.onPostExecute(result)
        val listener = mTaskListenerRef!!.get()
        listener?.onSettReceived(result)
    }

    interface TaskListener {
        fun onSettReceived(sett: Sett?)
    }
}*/
