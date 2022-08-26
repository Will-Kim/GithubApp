package com.talesapp.githubapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.SQLException

class DBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
): SQLiteOpenHelper(context, name, factory, version) {
    private val TAG = this.javaClass.simpleName

    override fun onCreate(db: SQLiteDatabase) {
        var sql: String = "CREATE TABLE if not exists GithubIssues (" +
                "id integer primary key not null, " +
                "org text not null, " +
                "repo text not null, " +
                "number integer, " +
                "title text, " +
                "body text, " +
                "url text, " +
                "login text, " +
                "avatar text)"
        try {
            db.execSQL(sql)
        }catch(sqle: SQLException) {
            Log.e(TAG, sqle.message.toString())
        }

    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql : String = "DROP TABLE if exists GithubIssues"

        try {
            db.execSQL(sql)
        } catch(sqle: SQLException) {
            Log.e(TAG, sqle.message.toString())
        }
        onCreate(db)
    }

}