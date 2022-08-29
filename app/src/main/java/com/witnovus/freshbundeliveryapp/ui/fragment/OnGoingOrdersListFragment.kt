package com.witnovus.freshbundeliveryapp.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.witnovus.nepzep.utils.Event
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.adapter.OnGoingOrderListAdapter
import com.witnovus.freshbundeliveryapp.databinding.OnGoingOrdersListFragmentBinding
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.model.orderdata.ongoingorderlist.Order
import com.witnovus.freshbundeliveryapp.ui.activity.NotDeliveredReasonActivity
import com.witnovus.freshbundeliveryapp.ui.base.BaseFragment
import com.witnovus.freshbundeliveryapp.utils.Constants
import com.witnovus.freshbundeliveryapp.utils.Utils
import com.witnovus.freshbundeliveryapp.viewmodel.orderdetail.OrderDetailViewModel
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class OnGoingOrdersListFragment : BaseFragment(), OnGoingOrderListAdapter.OnItemClickListener{


    private lateinit var binding: OnGoingOrdersListFragmentBinding
    private lateinit var mOnGoingOrderListAdapter: OnGoingOrderListAdapter
    private val upcomingOrderList = ArrayList<Order>()
    private val orderDetailViewModel by viewModel<OrderDetailViewModel>()

    private var completedDeliveryCount:Int = 0
    private var totalDeliveryCount:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_on_going_orders_list, container, false)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        getOrderDetailListObserver()

    }


    private fun getOrderDetailListObserver() {
        if (Utils.checkConnection(requireActivity())) {

            orderDetailViewModel.getOrderListDetailResponse(toString())

            if (!orderDetailViewModel.getOrderListDetailData.hasObservers()) {
                orderDetailViewModel.getOrderListDetailData.observe(requireActivity()) { getOrderListDetailData ->
                    if (getOrderListDetailData.success) {
                        Utils.hideProgress()
                        upcomingOrderList.clear()
                        if(getOrderListDetailData.results.orders.size !=0) {
                            upcomingOrderList.addAll(getOrderListDetailData.results.orders)
                            upcomingOrderList.reverse()

                            mOnGoingOrderListAdapter =
                                OnGoingOrderListAdapter(requireActivity(), upcomingOrderList, this)
                            binding.orderListRecyclerView.layoutManager =
                                LinearLayoutManager(requireContext())
                            binding.orderListRecyclerView.setHasFixedSize(true)
                            binding.orderListRecyclerView.adapter = mOnGoingOrderListAdapter
                            binding.orderListRecyclerView.visibility = View.VISIBLE
                            binding.emptyCartImgView.visibility = View.GONE
                        }else{
                            binding.orderListRecyclerView.visibility = View.GONE
                            binding.emptyCartImgView.visibility = View.VISIBLE
                        }

                        completedDeliveryCount =  getOrderListDetailData.results.orderTotals.deliveredOrders
                        totalDeliveryCount = getOrderListDetailData.results.orderTotals.totalOrders



                    }else{
                        binding.orderListRecyclerView.visibility = View.GONE
                        binding.emptyCartImgView.visibility = View.VISIBLE
                    }
                }
            }
            /**
             * observe for failed response from API
             */
            if (!orderDetailViewModel.loadingState.hasObservers()){
                orderDetailViewModel.loadingState.observe(requireActivity()) { loadingState ->
                    when (loadingState.status) {
                        LoadingState.Status.RUNNING -> {
                            if (!Utils.isProgressShowing()) {
                                Utils.showProgress(requireActivity())
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
                                    binding.orderListRecyclerView.visibility = View.GONE
                                    binding.emptyCartImgView.visibility = View.VISIBLE

                                } else {
                                    checkErrorCode(errorCode!!)
                                    Toast.makeText(
                                        requireActivity(),
                                        errorData.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        }
                    }
                }
            }
        }else{
            Toast.makeText(
                requireActivity(),
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
    }




    override fun onDeliveredClick(onGoingOrderList: Order, position: Int) {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.sub_delivered_order)
        builder.setMessage(R.string.delivered_order_desc)
        builder.setPositiveButton("Conform") { dialogInterface, _ ->
            dialogInterface.dismiss()
            orderDeliveredObserver(onGoingOrderList.type , onGoingOrderList.oRDERNUMBER)

        }
        builder.setNegativeButton("Cancel") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()


    }

    private fun orderDeliveredObserver(orderType:String,orderNumber:String) {

        val jsonObject = JSONObject()

        jsonObject.put("type", orderType)
        jsonObject.put("order_number", orderNumber)

        orderDetailViewModel.orderDeliveredResponse(jsonObject.toString())

        if (!orderDetailViewModel.orderDeliveredData.hasObservers()) {
            orderDetailViewModel.orderDeliveredData.observe(requireActivity()) { orderDeliveredData ->
                if (orderDeliveredData.success) {
                    Utils.hideProgress()
                    getOrderDetailListObserver()
                    (HomeFragment).deliveredOrderListCountHandledLivaData.postValue(Event(true))
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.msg_delivered_order),
                        Toast.LENGTH_SHORT
                    ).show()


                }
            }
        }
        /**
         * observe for failed response from API
         */
        if (!orderDetailViewModel.loadingState.hasObservers()){
            orderDetailViewModel.loadingState.observe(requireActivity()) { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(requireActivity())
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
                            if (errorMessage != null) {
                                Toast.makeText(
                                    requireActivity(),
                                    errorData.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }
                }
            }
        }
    }

    override fun onVacationClick(onGoingOrderList: Order, position: Int) {

        val bundle = Bundle()
        bundle.putString(Constants.SUBSCRIPTION_ORDER_ID, onGoingOrderList.orderId.toString())
        val vacationBottomSheetFragment = VacationModeBottomSheetDialogFragment()
        vacationBottomSheetFragment.arguments = bundle
        vacationBottomSheetFragment.show(
            requireActivity().supportFragmentManager,
            "vacationModeDialog"
        )

    }

    override fun onNotDeliveredOrderClick(onGoingOrderList: Order, position: Int) {
        val mIntent = Intent(requireActivity(), NotDeliveredReasonActivity::class.java)
        mIntent.putExtra("type",onGoingOrderList.type)
        mIntent.putExtra("order_number",onGoingOrderList.oRDERNUMBER)
        startActivity(mIntent)

    }

   /* override fun onNotDeliveredClick(onGoingOrderList: Order, position: Int) {

    }
*/
    override fun onCallImageClick(onGoingOrderList: Order, position: Int) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + onGoingOrderList.customerMobile)
        startActivity(dialIntent)
    }
}




