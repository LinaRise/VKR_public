package com.example.myapplication.database.repo

interface IRepository<T> {
    fun create(entity:T):Long
    fun update (entity:T)
    fun delete (entity:T)
    fun get (id:Long)
    fun getAll():List<T>?
}