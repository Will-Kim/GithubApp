package com.talesapp.githubapp

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

    }

    override fun onResume() {
        super.onResume()

        val call = RetrofitGithubIssuesCall(this@MainActivity)
        call.getIssues(viewModel, object : ApiListener {
             override fun onSuccess() {

             }

            override fun onFail(errMsg: String?) {
            }
        })
    }
}