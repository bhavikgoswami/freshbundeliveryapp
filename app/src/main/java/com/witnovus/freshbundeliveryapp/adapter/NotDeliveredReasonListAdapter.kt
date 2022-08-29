package com.witnovus.freshbundeliveryapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.model.NotDeliveredReasons


class NotDeliveredReasonListAdapter(
    private val mContext: Context,
    val reasonsList: ArrayList<NotDeliveredReasons>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<NotDeliveredReasonListAdapter.ViewHolder>() {

    fun updateList(newList: ArrayList<NotDeliveredReasons>){
        reasonsList.clear()
        reasonsList.addAll(newList)
    }

    interface OnItemClickListener {
        fun onReasonSelected(item: NotDeliveredReasons, position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reasonTitleTxtView: TextView = itemView.findViewById(R.id.reasonTitleTxtView)
        val rbReason: ImageView = itemView.findViewById(R.id.rbReason)
        val relRoot: RelativeLayout = itemView.findViewById(R.id.relRoot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_not_delivered_reason, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.reasonTitleTxtView.text = reasonsList[position].reason
        if (reasonsList[position].selected) {
            holder.rbReason.setImageResource(R.drawable.ic_radio_selected)
            holder.relRoot.setBackgroundResource(R.drawable.shape_yellow_rounded_corner_border)
        } else {
            holder.rbReason.setImageResource(R.drawable.ic_radio_default)
            holder.relRoot.setBackgroundResource(R.drawable.shape_gray_rounded_corner)
        }
        holder.relRoot.setOnClickListener {
            listener.onReasonSelected(reasonsList[position], position)
        }
    }

    override fun getItemCount(): Int {
        return reasonsList.size
    }
}