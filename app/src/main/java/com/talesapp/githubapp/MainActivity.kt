package com.talesapp.githubapp

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.talesapp.githubapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var mBroadcastReceiver: BroadcastReceiver
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Database 초기화
        Statics.initDB(this@MainActivity)
        viewModel = MainViewModel()

        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.issueList.layoutManager = LinearLayoutManager(this)

        binding.textOrgRepo.setOnClickListener(View.OnClickListener {
            changeOrgRepo()
        })


        val intent = Intent(this@MainActivity, GithubApiService::class.java)
        this@MainActivity.startService(intent)

        val filter = IntentFilter()
        filter.addAction(Statics.API_CALL_SUCCESS)
        filter.addAction(Statics.API_CALL_SUCCESS_TMP)

        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null)
                    when (intent.action) {
                        Statics.API_CALL_SUCCESS -> {
                            viewModel.org.value = Statics.getOrg()
                            viewModel.repo.value = Statics.getRepo()
                            viewModel.textOrgRepo.value = Statics.getOrg() + "/" + Statics.getRepo()
                            refreshAdapter(Statics.getOrg(), Statics.getRepo())
                        }
                        Statics.API_CALL_SUCCESS_TMP -> {
                            Statics.setOrg(Statics.tmpOrg!!)
                            Statics.setRepo(Statics.tmpRepo!!)
                            viewModel.org.value = Statics.getOrg()
                            viewModel.repo.value = Statics.getRepo()
                            viewModel.textOrgRepo.value = Statics.getOrg() + "/" + Statics.getRepo()
//                            refreshAdapter(Statics.getOrg(), Statics.getRepo())
                        }
                    }
            }
        }
        registerReceiver(mBroadcastReceiver, filter)
    }

    override fun onResume() {
        super.onResume()

        refreshAdapter(Statics.getOrg(), Statics.getRepo())
    }

    fun refreshAdapter(org:String, repo:String) {
        Log.e(TAG, "Refresh Success")
        val adapterIssues = AdapterIssues(Statics.getIssues(org, repo)!!)
        binding.issueList.adapter = adapterIssues
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver)
        }
    }

    private fun changeOrgRepo() {
        val textOrgRepo = EditText(this)
        val dialog: android.app.AlertDialog? = android.app.AlertDialog.Builder(this)
            .setTitle("Change Org/Repo")
            .setMessage("조회하고자하는 조직/리파지토리를 입력하세요")
            .setView(textOrgRepo)
            .setPositiveButton("변경",
                DialogInterface.OnClickListener { dialog, which ->
                    val text = textOrgRepo.text.toString()
                    val values = text.split("/")
                    if (values.size == 2) {
                        if (!values[0].isNullOrEmpty() && !values[1].isNullOrEmpty())
                            refreshAdapter(values[0], values[1])
                    }
                })
            .setNegativeButton("Cancel", null)
            .create()
        dialog!!.show()
    }

}