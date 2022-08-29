package com.witnovus.freshbundeliveryapp.adapter

import android.app.Notification
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.model.notification.notificationlist.Record

class NotificationAdapter(
    private val mContext: Context,
    private val notificationList: ArrayList<Record>,
    val listener: OnItemClickListener
): RecyclerView.Adapter<NotificationAdapter.ViewHolder>()  {

    interface OnItemClickListener {
        fun onNotificationClick(item: Record, position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notificationTitleTxtView: TextView =
            itemView.findViewById(R.id.notificationTitleTxtView)
        val notificationDescTxtView: TextView = itemView.findViewById(R.id.notificationDescTxtView)
        val notificationTimeTxtView: TextView = itemView.findViewById(R.id.notificationTimeTxtView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_notification, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notificationTitleTxtView.text = notificationList[position].title
        holder.notificationDescTxtView.text = notificationList[position].body
        holder.notificationTimeTxtView.text = notificationList[position].date
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}