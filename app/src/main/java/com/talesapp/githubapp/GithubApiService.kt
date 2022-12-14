package com.talesapp.githubapp

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GithubApiService : Service() {

    private val TAG = this.javaClass.simpleName.toString()
    lateinit var mBroadcastReceiver: BroadcastReceiver

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "GithubApiService created.")

        val filter = IntentFilter()
        filter.addAction(Statics.API_CALL_REQUEST)

        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null)
                    when (intent.action) {
                        Statics.API_CALL_REQUEST -> {
                            val call = RetrofitGithubIssuesCall(this@GithubApiService)
                            call.getIssues(Statics.tmpOrg!!, Statics.tmpRepo!!, object : ApiListener {
                                override fun onSuccess() {
                                    Log.e(TAG, "RetrofitGithubIssuesCall successfully")
                                    val intent = Intent()
                                    intent.action = Statics.API_CALL_SUCCESS_TMP
                                    this@GithubApiService.sendBroadcast(intent)
                                }

                                override fun onFail(errMsg: String?) {
                                    Log.e(TAG, "RetrofitGithubIssuesCall failed - $errMsg")
                                }
                            })
                        }
                    }
            }
        }
        registerReceiver(mBroadcastReceiver, filter)

    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "GithubApiService started")

        // Coroutine for every 5 minutes
        GlobalScope.launch(Dispatchers.IO) {
            while(true) {
                val call = RetrofitGithubIssuesCall(this@GithubApiService)
                call.getIssues(Statics.getOrg(), Statics.getRepo(), object : ApiListener {
                    override fun onSuccess() {
                        Log.e(TAG, "RetrofitGithubIssuesCall successfully")
                        val intent = Intent()
                        intent.action = Statics.API_CALL_SUCCESS
                        this@GithubApiService.sendBroadcast(intent)
                    }

                    override fun onFail(errMsg: String?) {
                        Log.e(TAG, "RetrofitGithubIssuesCall failed - $errMsg")
                    }
                })
                //5???: 5 * 60??? * 1000ms
                delay(300000)
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        Log.e(TAG, "GithubApiService ended")

        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver)
        }
    }
}