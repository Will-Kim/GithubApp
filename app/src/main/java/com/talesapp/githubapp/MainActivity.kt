package com.talesapp.githubapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.talesapp.githubapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var dbHelper : DBHelper
    lateinit var database : SQLiteDatabase
    private var mBroadcastReceiver: MBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = MainViewModel()
        setContentView(binding.root)
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.issueList.layoutManager = LinearLayoutManager(this)

        // Database 초기화
        Statics.initDB(this@MainActivity)

        val intent = Intent(this@MainActivity, GithubApiService::class.java)
        this@MainActivity.startService(intent)

        registBroadcastReceiver()
    }

    override fun onResume() {
        super.onResume()

        refreshAdapter()
    }

    fun refreshAdapter() {
        val adapterIssues = AdapterIssues(
            Statics.getIssues(viewModel.org.value!!, viewModel.repo.value!!)!!)
        binding.issueList.adapter = adapterIssues
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver)
            mBroadcastReceiver = null
        }
    }

    private fun registBroadcastReceiver() {
        if (mBroadcastReceiver == null) {
            val filter = IntentFilter()
            filter.addAction(Statics.API_CALL_SUCCESS)
            mBroadcastReceiver = MBroadcastReceiver()
            registerReceiver(mBroadcastReceiver, filter)
        }
    }



    class MBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) when (intent.action) {
                Statics.API_CALL_SUCCESS -> {
//                    refreshAdapter()
                }
            }
        }
    }
}