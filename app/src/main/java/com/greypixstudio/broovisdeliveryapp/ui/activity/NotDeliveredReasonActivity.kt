package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.adapter.NotDeliveredReasonListAdapter
import com.greypixstudio.broovisdeliveryapp.databinding.NotDeliveredReasonActivityBinding
import com.greypixstudio.broovisdeliveryapp.databinding.ToolbarLayoutBinding
import com.greypixstudio.broovisdeliveryapp.model.NotDeliveredReasons
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.ui.fragment.HomeFragment
import com.greypixstudio.broovisdeliveryapp.ui.fragment.OnGoingOrdersListFragment
import com.greypixstudio.broovisdeliveryapp.utils.Event
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.viewmodel.orderdetail.OrderDetailViewModel
import kotlinx.android.synthetic.main.activity_not_delivered_reason.*
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotDeliveredReasonActivity : BaseActivity(), NotDeliveredReasonListAdapter.OnItemClickListener {

    private lateinit var notDeliveredReasonListAdapter: NotDeliveredReasonListAdapter
    private val reasonList = ArrayList<NotDeliveredReasons>()
    lateinit var binding: NotDeliveredReasonActivityBinding
    private val orderDetailViewModel by viewModel<OrderDetailViewModel>()

    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding

    private var type  = ""
    private var orderNumber = ""
    private var reasonSelected = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_not_delivered_reason)
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type").toString()
        }
        if (intent.hasExtra("order_number")){
            orderNumber = intent.getStringExtra("order_number").toString()
        }
        init()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun init() {
        toolbarLayoutBinding = binding.notDeliveredToolbar
        binding.notDeliveredToolbar.toolbarNametxtView.text = getText(R.string.lbl_not_delivered)
        binding.notDeliveredToolbar.backACImageView.setOnClickListener {
            onBackPressed()
        }
        addDummyData()
        notDeliveredReasonListAdapter =
            NotDeliveredReasonListAdapter(this@NotDeliveredReasonActivity, reasonList, this)
        binding.reasonsRecyclerView.layoutManager =
            LinearLayoutManager(this@NotDeliveredReasonActivity)
        binding.reasonsRecyclerView.setHasFixedSize(true)
        binding.reasonsRecyclerView.adapter = notDeliveredReasonListAdapter

        binding.submitBtn.setOnClickListener {
            val title = reasonSelected
            val remarks = edtMessageBox.text.toString()

            if(title.isEmpty()) {
                Toast.makeText(
                    this@NotDeliveredReasonActivity,
                    "Please select reason",
                    Toast.LENGTH_SHORT
                ).show()
            }else if (remarks.isEmpty()){
                Toast.makeText(
                    this@NotDeliveredReasonActivity,
                    "Please enter comment",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                if (type.isNotEmpty()) {
                    if (orderNumber.isNotEmpty()) {
                        notDeliveredOrderObserver()
                        val jsonObject = JSONObject()

                        jsonObject.put("type", type)
                        jsonObject.put("order_number", orderNumber)
                        jsonObject.put("title", title)
                        jsonObject.put("remarks", remarks)

                        if (Utils.checkConnection(this@NotDeliveredReasonActivity)) {
                            orderDetailViewModel.notDeliveredOrderResponse(jsonObject.toString())
                        } else {
                            Toast.makeText(
                                this@NotDeliveredReasonActivity,
                                getString(R.string.msg_internet_connection_not_available),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }
        }
    }

    private fun notDeliveredOrderObserver() {
        if (!orderDetailViewModel.notDeliveredOrderData.hasObservers()) {
            orderDetailViewModel.notDeliveredOrderData.observe(this) { notDeliveredOrderData ->
                if (notDeliveredOrderData.success) {
                    Utils.hideProgress()
                    finish()
                    (HomeFragment).deliveredOrderListCountHandledLivaData.postValue(Event(true))
                    Toast.makeText(
                        this@NotDeliveredReasonActivity,
                        getString(R.string.msg_not_delivered_order),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        /**
         * observe for failed response from API
         */
        if (!orderDetailViewModel.loadingState.hasObservers()){
            orderDetailViewModel.loadingState.observe(this, androidx.lifecycle.Observer { loadingState ->
                when(loadingState.status){
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this)
                        }
                    }
                    LoadingState.Status.SUCCESS -> {
                        Utils.hideProgress()
                    }
                    LoadingState.Status.FAILED -> {
                        Utils.hideProgress()
                        val errorData = loadingState.errorData
                        errorData.let {
                            val errorCode = errorData?.errorCode
                            val errorMessage = errorData?.message
                            if (errorCode == 200) {
                                showToast(errorData.message)
                            } else {
                                checkErrorCode(errorCode!!)
                            }
                        }

                    }
                }
            })
        }
    }

   /* private fun onEnterMsgBoxFocus() {
        for (i in notDeliveredReasonListAdapter.reasonsList.indices) {
            notDeliveredReasonListAdapter.reasonsList[i].selected = false
        }
        notDeliveredReasonListAdapter.notifyDataSetChanged()

        //binding.rbReason.setImageResource(R.drawable.ic_radio_selected)
        binding.relMessageBox.setBackgroundResource(R.drawable.shape_yellow_rounded_corner_border)
    }*/


    fun addDummyData() {
        reasonList.add(NotDeliveredReasons("Vehicle Problem", true))
        reasonList.add(NotDeliveredReasons("Could not find and contact customer", false))
        reasonList.add(NotDeliveredReasons("Weather issue", false))
        reasonList.add(NotDeliveredReasons("Health issue", false))
        reasonList.add(NotDeliveredReasons("Other Reason",false))
        reasonSelected = reasonList[0].reason
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onReasonSelected(item: NotDeliveredReasons, position: Int) {
        //binding.rbReason.setImageResource(R.drawable.ic_radio_default)
        binding.relMessageBox.setBackgroundResource(R.drawable.shape_gray_rounded_corner)

        for (i in notDeliveredReasonListAdapter.reasonsList.indices) {
            notDeliveredReasonListAdapter.reasonsList[i].selected = position == i

            reasonSelected = notDeliveredReasonListAdapter.reasonsList[i].reason
        }

        notDeliveredReasonListAdapter.notifyDataSetChanged()
      //  hideKeyboard(binding.rbReason)
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}