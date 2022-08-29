package com.witnovus.nepzep.adapter.calBottom

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.BankListBinding
import com.witnovus.freshbundeliveryapp.model.uploaddocument.addbankdetail.banklist.Record
import com.witnovus.freshbundeliveryapp.utils.Constants
import java.util.*
import kotlin.collections.ArrayList


class BankListAdapter(

    val context: Context,
    val listener: BankListAdapter.OnItemClicked?
) : RecyclerView.Adapter<BankListAdapter.ViewHolder>(), Filterable {


    var bankList: ArrayList<Record?>? = ArrayList()
    var bankListFiltered: ArrayList<Record?>? = ArrayList()

    class ViewHolder(val dataBinding: BankListBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val dataBinding = DataBindingUtil.inflate<BankListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_layout_bank_list, parent, false
        )
        return ViewHolder(dataBinding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.dataBinding.apply {
            val bank = bankListFiltered!!.get(position)

            if (bank!!.bankLogo != null && bank.bankLogo!!.isNotEmpty()) {
                val bankImgage = Constants.BASE_URL_IMAGE + bank.bankLogo
                Glide.with(context)
                    .load(bankImgage)
                    .error(R.drawable.ic_thumbnail)
                    .placeholder(R.drawable.ic_thumbnail)
                    .into(bankImgView)

            }
            if (bank != null && bank.bankName != null && !bank.bankName.isNullOrEmpty()) {
                bankTitleTxtView.setText(bank.bankName)
            }

            if (bank.isSelected) {
                rootLnrLayout.background =
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.shape_yellow_rounded_corner_border
                    )

            } else {
                rootLnrLayout.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.shape_gray_rounded_corner
                )

            }
            rootLnrLayout.setOnClickListener {
                for (prod in bankList!!) {
                    prod!!.isSelected = false
                }
                bankList?.get(position)?.isSelected = true
                bankList?.get(position)?.let { it1 -> listener!!.onItemClick(it1, position) }

                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return bankListFiltered?.size!!
    }

    interface OnItemClicked {
        fun onItemClick(bank: Record?, position: Int)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                bankListFiltered = if (constraint!!.isEmpty()) {
                    bankList
                } else {
                    val filteredList: ArrayList<Record?> = ArrayList()

                    bankList!!.filter {
                        it!!.bankName!!.lowercase(Locale.getDefault())
                            .contains(constraint.toString().lowercase(Locale.getDefault()))
                    }.forEach { filteredList.add(it!!) }
                    filteredList
                }
                return FilterResults().apply { values = bankListFiltered }
            }


            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                bankListFiltered = results?.values as ArrayList<Record?>?
                notifyDataSetChanged()
            }
        }
    }


    fun addData(list: List<Record?>?) {
        bankList = list as ArrayList<Record?>?
        bankListFiltered = bankList
        notifyDataSetChanged()
    }

}