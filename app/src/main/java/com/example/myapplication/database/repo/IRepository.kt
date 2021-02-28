package com.example.myapplication.database.repo

interface IRepository<T> {
   fun create(entity:T):Long
    fun update (entity:T): Long
    fun delete (entity: T):Long
    fun get (id:Long):T?
    fun getAll():List<T>?
}