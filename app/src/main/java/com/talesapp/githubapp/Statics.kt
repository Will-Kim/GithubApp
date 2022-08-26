package com.talesapp.githubapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.net.URLEncoder
import java.sql.SQLException

class Statics {
    companion object {
        lateinit var dbHelper: DBHelper
        lateinit var database: SQLiteDatabase
        lateinit var ctx: Context
        private val prefName = "__GithubApp__"
        private val prefOrg = "__GithubApp_Org__"
        private val prefRepo = "__GithubApp_Repo__"
        private val TAG = this.javaClass.simpleName

        init {

        }

        fun initDB(context: Context) {
            ctx = context
            dbHelper = DBHelper(ctx, "github_issues.db", null, 2)
            database = dbHelper.writableDatabase

        }

        fun insertIssues(issues: List<ResponseIssue>) {
            for (issue:ResponseIssue in issues) {
                insertIssue(issue)
            }
        }

        private fun insertIssue(issue: ResponseIssue) {
            val sql = "INSERT INTO " +
                    "GithubIssues(id, org, repo, number, title, body, url, login, avatar) " +
                    "VALUES (${issue.id}, '${getOrg()}', '${getRepo()}', ${issue.number}, " +
                    "'${URLEncoder.encode(issue.title, "utf-8")}', " +
                    "'${URLEncoder.encode(issue.body, "utf-8")}', " +
                    "'${issue.url}', '${issue.user?.login}', " +
                    "'${issue.user?.avatar}')"
            Log.e(TAG, sql)

            try {
                if (!CheckIsDataAlreadyInDBorNot(issue.id!!)) {
                    Log.e(TAG, "DB에 없음: 추가")
                    database.execSQL(sql, arrayOf())
                    getRowById(issue.id)
                } else {
                    Log.e(TAG, "DB에 존재: 스킵")
                }

            }catch(sqle: SQLException) {
                Log.e(TAG, sqle.message.toString())
                sqle.printStackTrace()
            }
        }

        @SuppressLint("Range")
        fun getRowById(id: Long){
            val selectQuery = "SELECT  * FROM GithubIssues WHERE id = ?"
            database.rawQuery(selectQuery, arrayOf(id.toString())).use { // .use requires API 16
                if (it.moveToFirst()) {
                    var id_ = it.getLong(it.getColumnIndex("id"))
                    var out = id_.toString() + " "
                    out += it.getString(it.getColumnIndex("repo"))
                    Log.e(TAG, out)
                }
            }
        }


        fun CheckIsDataAlreadyInDBorNot(id: Long): Boolean {
            val query = "SELECT  * FROM GithubIssues WHERE id = ?"
            val cursor: Cursor = database.rawQuery(query, arrayOf(id.toString()))
            if (cursor.count <= 0) {
                cursor.close()
                return false
            }
            cursor.close()
            return true
        }

        fun getOrg(): String {
            val pref: SharedPreferences = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(prefOrg, "google").toString()
        }

        fun getRepo(): String {
            val pref: SharedPreferences = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(prefRepo, "dagger").toString()
        }

        fun setOrg(value: String) {
            if (!value.isNullOrEmpty()) {
                val pref: SharedPreferences =
                    ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                pref.edit().putString(prefOrg, value)
            }
        }

        fun setRepo(value: String) {
            if (!value.isNullOrEmpty()) {
                val pref: SharedPreferences =
                    ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                pref.edit().putString(prefRepo, value)
            }
        }

    }
}