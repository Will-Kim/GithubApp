package com.talesapp.githubapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val TAG = this.javaClass.simpleName
    var textOrgRepo = MutableLiveData<String>()
    var org = MutableLiveData<String>()
    var repo = MutableLiveData<String>()

    init {
        org.value = Statics.getOrg()
        repo.value = Statics.getRepo()
        textOrgRepo.value = Statics.getOrg() + "/" + Statics.getRepo()
    }

    fun setIssues(responses: List<ResponseIssue>) {
//        ResponseIssuevar issues = Gson().fromJson(responses, Array<ResponseIssue>::class.java).toList()
        for(issue: ResponseIssue in responses) {
            Log.e(TAG, "==> $issue")
        }
    }
}