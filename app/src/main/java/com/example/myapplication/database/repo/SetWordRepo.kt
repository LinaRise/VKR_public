package com.example.myapplication.database.repo

import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.SetWord
import com.example.myapplication.entity.Word

class SetWordRepo(val dbhelper: DBHelper):IRepository<SetWord>  {
    override fun create(entity: SetWord): Long {
        TODO("Not yet implemented")
    }

    override fun update(entity: SetWord) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: SetWord) {
        TODO("Not yet implemented")
    }

    override fun get(id: Long): SetWord {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<SetWord>? {
        TODO("Not yet implemented")
    }


}