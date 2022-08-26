package com.talesapp.githubapp

interface ApiListener {
    fun onSuccess()
    fun onFail(errMsg: String?=null)
}