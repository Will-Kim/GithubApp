package com.talesapp.githubapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

class MainViewModel: ViewModel() {
    private val TAG = this.javaClass.simpleName
    var textOrgRepo = MutableLiveData<String>()
    var org = MutableLiveData<String>()
    var repo = MutableLiveData<String>()

    init {
        org.value = "google"
        repo.value = "dagger"
        textOrgRepo.value = "google/dagger"
    }

    fun setIssues(responses: List<ResponseIssue>) {
//        ResponseIssuevar issues = Gson().fromJson(responses, Array<ResponseIssue>::class.java).toList()
        for(issue: ResponseIssue in responses) {
            Log.e(TAG, "==> $issue")
        }
    }
}