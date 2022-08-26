package com.talesapp.githubapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.net.URLEncoder
import java.sql.SQLException

class Statics {
    companion object {
        private var spref: SharedPreferences? = null
        const val API_CALL_SUCCESS_TMP: String = "API_CALL_SUCCESS_TMP"
        const val API_CALL_SUCCESS: String = "API_CALL_SUCCESS"
        const val API_CALL_REQUEST: String = "API_CALL_REQUEST"
        var currentIssue: GithubIssue? = null
        lateinit var dbHelper: DBHelper
        lateinit var database: SQLiteDatabase
        lateinit var ctx: Context
        private val prefName = "__GithubApp__"
        private val prefOrg = "__GithubApp_Org__"
        private val prefRepo = "__GithubApp_Repo__"
        private val TAG = this.javaClass.simpleName
        var tmpOrg: String? = ""
        var tmpRepo: String? = ""

        init {

        }

        fun initDB(context: Context) {
            ctx = context
            dbHelper = DBHelper(ctx, "github_issues.db", null, 2)
            database = dbHelper.writableDatabase
            spref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
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

        @SuppressLint("Range")
        fun getIssues(org:String, repo:String): ArrayList<GithubIssue>? {
            val issues: ArrayList<GithubIssue> = ArrayList()

            val cursor = database.rawQuery(
                "SELECT * FROM GithubIssues WHERE org = ? and repo = ? ", arrayOf(org, repo))
            if (cursor.count > 0) {
                setOrg(org)
                setRepo(repo)
                if (cursor.moveToFirst()) {
                    var idx = 0
                    do {
                        idx++
                        if (idx == 5) {
                            var issue = GithubIssue()
                            issue.url = "https://thingsflow.com/ko/home"
                            issue.avatar =
                                "https://s3.ap-northeast-2.amazonaws.com/hellobot-kr-test/image/main_logo.png"
                            issues.add(issue)
                        }
                        var issue = GithubIssue()
                        issue.id = cursor.getLong(cursor.getColumnIndex("id"))
                        issue.number = cursor.getInt(cursor.getColumnIndex("number"))
                        issue.title = cursor.getString(cursor.getColumnIndex("title"))
                        issue.body = cursor.getString(cursor.getColumnIndex("body"))
                        issue.url = cursor.getString(cursor.getColumnIndex("url"))
                        issue.login = cursor.getString(cursor.getColumnIndex("login"))
                        issue.avatar = cursor.getString(cursor.getColumnIndex("avatar"))
                        issue.type = "GITHUB_ISSUE"
                        issues.add(issue)
                    } while (cursor.moveToNext())
                }
            } else {
                tmpOrg = org
                tmpRepo = repo
                val intent = Intent()
                intent.action = API_CALL_REQUEST
                ctx.sendBroadcast(intent)
            }
            cursor.close()
            return issues
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
            return spref?.getString(prefOrg, "google").toString()
        }

        fun getRepo(): String {
            return spref?.getString(prefRepo, "dagger").toString()
        }

        fun setOrg(value: String) {
            if (!value.isNullOrEmpty()) {
                spref?.edit()?.putString(prefOrg, value)?.apply()
//                spref?.edit()?.apply()
            }
        }

        fun setRepo(value: String) {
            if (!value.isNullOrEmpty()) {
                spref?.edit()?.putString(prefRepo, value)?.apply()
//                spref?.edit()?.apply()
            }
        }

    }
}