package com.google.firebase.udacity.friendlychat.repository

/**
 * Created by jorge_caro on 2/9/18.
 */
interface Repository<T> {
    fun query(): List<T>
    fun insert(entity: T)
    fun update(entity: T)
    fun delete(entity: T)
}