package com.talesapp.githubapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.net.URLDecoder


class AdapterIssues(private val mList: List<GithubIssue>) : RecyclerView.Adapter<AdapterIssues.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_issue, parent, false)

        return ViewHolder(view)
    }

    // 아이템을 뷰에 적용
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val issue = mList[position]

        if (!issue.type.equals("GITHUB_ISSUE")) {
            Glide.with(Statics.ctx)
                .load(issue.avatar)
                .into(holder.issueImage)
            holder.issueImage.visibility = View.VISIBLE
            holder.issueImage.setOnClickListener(View.OnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(issue.url))
                Statics.ctx.startActivity(browserIntent)
            })
        } else {
            holder.issueText.text = "#" + issue.number + " " + URLDecoder.decode(issue.title, "utf-8")
            holder.issueText.setOnClickListener(View.OnClickListener {
                Statics.currentIssue = issue
                val myIntent = Intent(Statics.ctx, IssueActivity::class.java)
                Statics.ctx.startActivity(myIntent)
            })
        }


    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val issueImage: ImageView = itemView.findViewById(R.id.issueImage)
        val issueText: TextView = itemView.findViewById(R.id.issueText)
    }
}