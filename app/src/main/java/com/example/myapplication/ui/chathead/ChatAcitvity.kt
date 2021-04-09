package com.example.myapplication.ui.chathead


import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.setView.CopyCardDialog
import com.example.myapplication.ui.setView.SetViewPresenter


class ChatActivity : AppCompatActivity(), IChatActivityView {

//    private var textView: TextView? = null

    private lateinit var clipboard: ClipboardManager
    lateinit var presenter: ChatActivityPresenter
    lateinit var dbhelper: DBHelper

    private fun getCopy(): String {
//        textView = findViewById<TextView>(R.id.textview)
        val copiedText = clipboard.getClipboardText(this)
//                textView!!.text = words + "\n"
        return copiedText ?: ""

//        if (clipboard.hasPrimaryClip() && clipboard.primaryClip != null) {
//            var clip = clipboard.primaryClip!!.getItemAt(0).toString()
//            textView!!.text = clip.toString();
//        } else {
//            textView!!.text = "Gecnj";
//
//        }
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
        clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        dbhelper = DBHelper(this)
        presenter = ChatActivityPresenter(this, dbhelper)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            getCopy()
            val sets = presenter.getAllSetsTitles()
            Log.d("Sets", sets!!.size.toString())
            val copiedTextAddDialog =
                CopiedTextAddDialog(sets, Word(originalWord = getCopy(), translatedWord = ""), null, dbhelper)
            copiedTextAddDialog.show(supportFragmentManager, "Add copied word")
        }
    }
}