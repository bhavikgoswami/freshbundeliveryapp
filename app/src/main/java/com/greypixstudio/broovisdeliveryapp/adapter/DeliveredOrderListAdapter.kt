package com.greypixstudio.broovisdeliveryapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.AdapterDeliveredOrderListBinding
import com.greypixstudio.broovisdeliveryapp.model.orderdata.deliveredorderlist.Record
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import kotlinx.android.synthetic.main.row_product_order_list.view.*

class DeliveredOrderListAdapter(
    private val mContext: Context,
    private val deliveredOrderList: ArrayList<Record>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<DeliveredOrderListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onVacationClick(deliveredOrderList: Record, position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderTypeTxtView: TextView = itemView.findViewById(R.id.orderTypeTxtView)
        val customerNameTxtView: TextView = itemView.findViewById(R.id.customerNameTxtView)
        val deliveryTimeTxtView: TextView = itemView.findViewById(R.id.deliveryTimeTxtView)
        val totalQualityOrderTextView: TextView = itemView.findViewById(R.id.totalQualityTxtView)
        val orderIdTxtView: TextView = itemView.findViewById(R.id.orderIdTxtView)
        val addressTxtView: TextView = itemView.findViewById(R.id.addressTxtView)
        val itemProductListLnrLayout: LinearLayout =
            itemView.findViewById(R.id.itemProductListLnrLayout)
        val deliveredImageView: ImageView = itemView.findViewById(R.id.deliveredImageView)
        val deliverTxtView: TextView = itemView.findViewById(R.id.deliverTxtView)
        val notDeliverImageView: ImageView = itemView.findViewById(R.id.notDeliverImageView)
        val notDeliverTxtView: TextView = itemView.findViewById(R.id.notDeliverTxtView)
        val relVacation: RelativeLayout = itemView.findViewById(R.id.relVacation)
        val amountTextView: AppCompatTextView = itemView.findViewById(R.id.amounttTxtView)
        val relAmount:
                RelativeLayout = itemView.findViewById(R.id.amountRltLayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_delivered_order_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.customerNameTxtView.text = deliveredOrderList[position].customerName
        holder.deliveryTimeTxtView.text = deliveredOrderList[position].deliveryTime
        holder.totalQualityOrderTextView.text =
            deliveredOrderList[position].totalQty.toInt().toString()
        holder.orderIdTxtView.text = deliveredOrderList[position].orderId.toString()
        holder.addressTxtView.text = deliveredOrderList[position].address
        if (deliveredOrderList[position].paymentBy == Constants.WALLET_AND_CASH_ON_DELIVERY
            || deliveredOrderList[position].paymentBy == Constants.CASH_ON_DELIVERY
        )

            if (deliveredOrderList[position].totalCash != null) {
                holder.relAmount.visibility = View.VISIBLE
                holder.amountTextView.text =
                    mContext.getString(R.string.lbl_rs) + Constants.BLANK_SPACE + deliveredOrderList[position].totalCash
            } else {
                holder.relAmount.visibility = View.GONE
            }

        if (deliveredOrderList[position].deliveryStatus == Constants.ORDER_DELIVERED) {
            holder.deliveredImageView.visibility = View.VISIBLE
            holder.deliverTxtView.visibility = View.VISIBLE
            holder.notDeliverImageView.visibility = View.GONE
            holder.notDeliverTxtView.visibility = View.GONE

        }
        if (deliveredOrderList[position].deliveryStatus == Constants.ORDER_NOT_DELIVERED) {
            holder.deliveredImageView.visibility = View.GONE
            holder.deliverTxtView.visibility = View.GONE
            holder.notDeliverImageView.visibility = View.VISIBLE
            holder.notDeliverTxtView.visibility = View.VISIBLE
        }
        holder.relVacation.setOnClickListener {
            listener.onVacationClick(
                deliveredOrderList[position],
                position
            )
        }

        if (deliveredOrderList[position].type.equals(Constants.CART_ORDER)) {
            holder.relVacation.visibility = View.GONE
            holder.orderTypeTxtView.setText(Constants.ONE_TIME_BUYER)
        }
        if (deliveredOrderList[position].type.equals(Constants.TOPUP_ORDER)) {
            holder.relVacation.visibility = View.GONE
            holder.orderTypeTxtView.setText(Constants.ONE_TIME_BUYER)
        }
        if (deliveredOrderList[position].type.equals(Constants.SUBSCRIPTION_ORDER)) {
            holder.relVacation.visibility = View.VISIBLE
            holder.orderTypeTxtView.setText(Constants.SUBSCRIBER)
        }

        for (products in deliveredOrderList[position].products) {

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
        return deliveredOrderList.size
    }
}