package com.talesapp.githubapp

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.util.Log

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
            dbHelper = DBHelper(ctx, "github_issues.db", null, 1)
            database = dbHelper.writableDatabase

        }

        fun insertIssues(issues: List<ResponseIssue>) {
            for (issue:ResponseIssue in issues) {
                insertIssue(issue)
            }
        }

        fun insertIssue(issue: ResponseIssue) {
            val sql = "INSERT INTO " +
                    "GithubIssues(id, ord, repo, number, title, body, url, login, avatar) " +
                    "SELECT ${issue.id}, '${getOrg()}', '${getRepo()}', ${issue.number}, " +
                    "'${issue.title}', '${issue.body}', '${issue.url}', '${issue.user?.login}'," +
                    " '${issue.user?.avatar}' " +
                    "WHERE  NOT EXISTS(SELECT 1 FROM GithubIssues WHERE id = ${issue.id})"
            Log.e(TAG, sql)
            database.execSQL(sql)
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