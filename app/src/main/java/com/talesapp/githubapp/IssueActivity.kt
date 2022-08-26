package com.talesapp.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.net.URLDecoder

class IssueActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue)

        if (Statics.currentIssue != null) {
            val issueImg = findViewById<ImageView>(R.id.issueImage)
            val issueUser = findViewById<TextView>(R.id.issueText)
            val issueBody = findViewById<TextView>(R.id.issueBody)

            Glide.with(this)
                .load(Statics.currentIssue!!.avatar)
                .into(issueImg)

            issueUser.text = Statics.currentIssue!!.login
            issueBody.text = URLDecoder.decode(Statics.currentIssue!!.body, "utf-8")

        }
    }
}