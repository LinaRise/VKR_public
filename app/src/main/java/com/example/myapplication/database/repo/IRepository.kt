package com.example.myapplication.database.repo

interface IRepository<T> {
    fun create(entity: T): Any
    fun update(entity: T): Any
    fun delete(entity: T): Any
    fun get(id: Long): T?
    fun getAll(): List<T>?
}