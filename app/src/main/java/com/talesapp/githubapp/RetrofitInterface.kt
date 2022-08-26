package com.talesapp.githubapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface RetrofitInterface {
    @GET("{org}/{repo}/issues")
    fun getIssues(
        @Path("org") org: String?,
        @Path("repo") repo: String?
    ): Call<List<ResponseIssue>>
}