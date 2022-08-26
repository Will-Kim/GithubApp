package com.talesapp.githubapp

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.net.SocketTimeoutException
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import java.io.IOException

class RetrofitGithubIssuesCall(context: Context) {
    private val TAG = this.javaClass.simpleName
    var ctx : Context? = null

    init {
        if (context != null) {
            this.ctx = context
        }
    }

    fun getIssues(viewModel: MainViewModel, apiListener: ApiListener) {
        if (viewModel != null) {
            val retrofitInterface: RetrofitInterface =
                RetrofitInstance.getRetrofitInstance()!!.create<RetrofitInterface>(
                    RetrofitInterface::class.java
                )
//            val api = retrofitInterface.getIssues(viewModel.org.value, viewModel.repo.value)
            val api = retrofitInterface.getIssues("google", "dagger")
            api.enqueue(object : Callback<List<ResponseIssue>> {
                override fun onResponse(
                    call: Call<List<ResponseIssue>>?,
                    response: Response<List<ResponseIssue>>?
                ) {
                    val resp = response?.body()
                    Log.e(TAG, resp.toString())
                    if (resp != null) {
                        viewModel.setIssues(resp)
//                        viewModel.setTropiesData(resp.data)
//                        if (resp.data.hearts != null) Statics.setHearts(ctx, resp.data.hearts)
                        Log.e("IssuesCall", resp.toString())
                        apiListener.onSuccess()
                    } else {
                        val gson = Gson()
                        var errMsg = ""
                        try {
                            errMsg = "Error"
                        } catch (e: IOException) {
                            Log.e("TrophyCall", "IOException: " + e.message)
                        }
                        apiListener.onFail(errMsg)
                    }
                }

                override fun onFailure(call: Call<List<ResponseIssue>>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        apiListener.onFail("NetworkProblem " + t.message)
                        Log.e("TrophyCall", "TIMEOUT: " + t.message)
                    } else {
                        apiListener.onFail("NetworkProblem " + t.message)
                        Log.e("TrophyCall", "Error: " + t.message)
                    }
                }
            })
        }

    }


}

private fun <T> Call<T>.enqueue(callback: Callback<List<ResponseIssue>>) {

}
