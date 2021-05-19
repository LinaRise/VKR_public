package com.example.myapplication.ui.chathead


import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import java.util.concurrent.Executors


class ChatActivity : AppCompatActivity(), IChatActivityView {

    private lateinit var clipboard: ClipboardManager
    lateinit var presenter: ChatActivityPresenter
    lateinit var dbhelper: DBHelper

    private fun getCopy(): String {
        val copiedText = clipboard.getClipboardText(this)
        return copiedText ?: ""
    }

    private fun ClipboardManager.getClipboardText(context: Context): String? {
        if (hasPrimaryClip()) {
            val clip = primaryClip
            if (clip != null && clip.itemCount > 0) {
                val clipboardTextItem = clip.getItemAt(0).coerceToText(context)

                if (clipboardTextItem != null)
                    return clipboardTextItem.toString()
            }
        }
        return null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //буфер обмена
        clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        dbhelper = DBHelper(this)
        presenter = ChatActivityPresenter(dbhelper)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            var sets: List<Sett>?
            executor.execute {
                sets = presenter.getAllSetsTitles()
                handler.post {
                    Log.d("Sets got", sets!!.size.toString())
                    val copiedTextAddDialog =
                        CopiedTextAddDialog(
                            sets,
                            Word(originalWord = getCopy(), translatedWord = ""),
                            null,
                            dbhelper
                        )
                    copiedTextAddDialog.show(supportFragmentManager, "Add copied word")
                }
            }



        }
    }
}