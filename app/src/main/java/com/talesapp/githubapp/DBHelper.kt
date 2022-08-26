package com.talesapp.githubapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
): SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        var sql: String = "CREATE TABLE if not exists GithubIssues (" +
                "id long primary key not null, " +
                "org text not null, " +
                "repo text not null, " +
                "number long not null, " +
                "title text not null, " +
                "body text not null, " +
                "login text not null, " +
                "avatar text not null"
        db.execSQL(sql)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql : String = "DROP TABLE if exists GithubIssues"

        db.execSQL(sql)
        onCreate(db)
    }

}