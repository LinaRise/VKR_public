package com.example.myapplication.ui.chathead


import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R


class ChatActivity : AppCompatActivity() {

    private var textView: TextView? = null

    private lateinit var clipboard: ClipboardManager

    private fun getCopy() {
        textView = findViewById<TextView>(R.id.textview)
        val words = clipboard.getClipboardText(this)
                textView!!.text = words + "\n"


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


    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            getCopy()
    }
}