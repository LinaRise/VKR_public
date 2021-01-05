package com.example.myapplication.ui.setAdd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Word

class SetCreateActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var setCreateAdapter:SetCreateAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_create)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById(R.id.recyclerivew_set_create)
        recyclerView.layoutManager = LinearLayoutManager(this)
        setCreateAdapter = SetCreateAdapter(this,getWordList())
        recyclerView.adapter = setCreateAdapter

    }

    private fun getWordList(): ArrayList<Word> {
        var words = ArrayList<Word>()
        var word1 = Word("1","1")
        var word2 = Word("2","2")
        var word3 = Word("3","3")
        var word4 = Word("4","4")
        var word5 = Word("5","6")
        words.add(word1)
        words.add(word2)
        words.add(word3)
        words.add(word4)
        words.add(word5)
        return words
    }
}