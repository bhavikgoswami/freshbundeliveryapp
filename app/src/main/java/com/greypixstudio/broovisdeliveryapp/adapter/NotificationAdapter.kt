package com.greypixstudio.broovisdeliveryapp.adapter

import android.app.Notification
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.model.notification.notificationlist.Record
import com.greypixstudio.broovisdeliveryapp.utils.Constants

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
        val markImageView: AppCompatImageView = itemView.findViewById(R.id.markImageView)
        val contentImgView: AppCompatImageView = itemView.findViewById(R.id.contentImgView)
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
        if (notificationList[position].image_url.isNotEmpty()) {
            holder.contentImgView.visibility = View.VISIBLE
            Glide.with(mContext)
                .load(Constants.BASE_URL_IMAGE + notificationList[position].image_url)
                .error(R.drawable.ic_thumbnail)
                .placeholder(R.drawable.ic_thumbnail)
                .into(holder.contentImgView)
        } else {
            holder.contentImgView.visibility = View.GONE
        }
        if (notificationList[position].read_status == Constants.YES) {
            holder.markImageView.visibility = View.GONE
        } else {
            holder.markImageView.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            listener.onNotificationClick(notificationList[position], position)
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}