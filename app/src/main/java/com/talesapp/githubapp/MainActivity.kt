package com.talesapp.githubapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.talesapp.githubapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = MainViewModel()
        setContentView(binding.root)

        // Database 초기화
        Statics.initDB(this@MainActivity)

        val intent = Intent(this@MainActivity, GithubApiService::class.java)
        this@MainActivity.startService(intent)
    }

    override fun onResume() {
        super.onResume()

//        val call = RetrofitGithubIssuesCall(this@MainActivity)
//        call.getIssues(viewModel, object : ApiListener {
//             override fun onSuccess() {
//
//             }
//
//            override fun onFail(errMsg: String?) {
//            }
//        })
    }
}