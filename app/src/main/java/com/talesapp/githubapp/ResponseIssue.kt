package com.talesapp.githubapp

class ResponseIssue {
    val url: String? = null
    val id: String? = null
    val number: Int? = 0
    val title: String? = null
    val body: String? = null

    val user: User?=null

    override fun toString(): String {
        return "Issue(url=$url, id=$id, number=$number, title=$title, user=$user)"
    }


    class User {
        val login: String?=null
        val avatar: String?=null
        override fun toString(): String {
            return "User(login=$login, avatar=$avatar)"
        }


    }
}