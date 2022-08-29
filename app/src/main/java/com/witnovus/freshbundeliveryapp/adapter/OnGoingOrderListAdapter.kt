package com.witnovus.freshbundeliveryapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.model.orderdata.ongoingorderlist.Order
import com.witnovus.freshbundeliveryapp.utils.Constants
import kotlinx.android.synthetic.main.row_product_order_list.view.*


class OnGoingOrderListAdapter(
    private val mContext: Context,
    private val onGoingOrderList: ArrayList<Order>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<OnGoingOrderListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onDeliveredClick(onGoingOrderList: Order, position: Int)
        fun onVacationClick(onGoingOrderList: Order, position: Int)
        fun onNotDeliveredOrderClick(onGoingOrderList: Order, position: Int)
        fun onCallImageClick(onGoingOrderList: Order, position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderTypeTxtView:TextView = itemView.findViewById(R.id.orderTypeTxtView)
        val customerNameTxtView: TextView = itemView.findViewById(R.id.customerNameTxtView)
        val deliveryTimeTxtView: TextView = itemView.findViewById(R.id.deliveryTimeTxtView)
        val totalQualityOrderTextView: TextView = itemView.findViewById(R.id.totalQualityTxtView)
        val orderIdTxtView: TextView = itemView.findViewById(R.id.orderIdTxtView)
        val addressTxtView: TextView = itemView.findViewById(R.id.addressTxtView)
        val callImgView: ImageView = itemView.findViewById(R.id.callImgView)
        val relDelivered: RelativeLayout = itemView.findViewById(R.id.relDelivered)
        val relVacation: RelativeLayout = itemView.findViewById(R.id.relVacation)
        val relNotDeliverOrder: RelativeLayout = itemView.findViewById(R.id.relNotDeliverOrder)
        val mapLnrLayout: LinearLayout = itemView.findViewById(R.id.mapLnrLayout)
        val itemProductListLnrLayout:LinearLayout = itemView.findViewById(R.id.itemProductListLnrLayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_ongoing_order, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.customerNameTxtView.text = onGoingOrderList[position].customerName
        holder.deliveryTimeTxtView.text = onGoingOrderList[position].deliveryTime
        holder.totalQualityOrderTextView.text = onGoingOrderList[position].totalQty.toInt().toString()
        holder.orderIdTxtView.text = onGoingOrderList[position].orderId.toString()
        holder.addressTxtView.text = onGoingOrderList[position].address
        val latitudes = onGoingOrderList[position].latitudes
        val longitudes = onGoingOrderList[position].longitudes


        holder.mapLnrLayout.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+latitudes+","+longitudes)
            )
            mContext.startActivity(intent)
        }

        holder.relDelivered.setOnClickListener {
            listener.onDeliveredClick(
                onGoingOrderList[position],
                position
            )
        }
        holder.relVacation.setOnClickListener {
            listener.onVacationClick(
                onGoingOrderList[position],
                position
            )
        }
        holder.relNotDeliverOrder.setOnClickListener {
            listener.onNotDeliveredOrderClick(
                onGoingOrderList[position],
                position
            )
        }
        holder.callImgView.setOnClickListener {
            listener.onCallImageClick(
                onGoingOrderList[position],
                position
            )
        }
        if(onGoingOrderList[position].type.equals(Constants.CART_ORDER)){
            holder.relVacation.visibility = View.GONE
            holder.orderTypeTxtView.setText(Constants.ONE_TIME_BUYER)
        }
        if (onGoingOrderList[position].type.equals(Constants.TOPUP_ORDER)){
            holder.relVacation.visibility = View.GONE
            holder.orderTypeTxtView.setText(Constants.ONE_TIME_BUYER)
        }
        if (onGoingOrderList[position].type.equals(Constants.SUBSCRIPTION_ORDER)){
            holder.relVacation.visibility = View.VISIBLE
            holder.orderTypeTxtView.setText(Constants.SUBSCRIBER)
        }
        for (products in onGoingOrderList[position].products) {

            val mView: View = LayoutInflater.from(mContext)
                .inflate(R.layout.row_product_order_list, holder.itemProductListLnrLayout, false)
            val productImgView = mView.findViewById(R.id.productImgView) as ImageView
            val productNameTxtView = mView.findViewById(R.id.productNameTxtView) as TextView
            val unitValueTxtView = mView.findViewById(R.id.unitValueTxtView) as TextView
            val unitTxtView = mView.findViewById(R.id.unitTxtView) as TextView
            val qtyTxtView = mView.findViewById(R.id.qtyTxtView) as TextView

            mView.productNameTxtView.text = products.productName
            mView.unitValueTxtView.text = products.unitValue.toInt().toString()
            mView.unitTxtView.text = products.unit
            mView.qtyTxtView.text = products.qty.toInt().toString()
            Glide.with(mContext)
                .load(Constants.BASE_URL_IMAGE.plus(products.productImage))
                .error(R.drawable.ic_thumbnail)
                .placeholder(R.drawable.ic_thumbnail)
                .into(productImgView)


            holder.itemProductListLnrLayout.addView(mView)
        }
    }

    override fun getItemCount(): Int {
        return onGoingOrderList.size
    }
}