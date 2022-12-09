package com.greypixstudio.broovisdeliveryapp.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.adapter.OnGoingOrderListAdapter
import com.greypixstudio.broovisdeliveryapp.databinding.OnGoingOrdersListFragmentBinding
import com.greypixstudio.broovisdeliveryapp.model.JsonConstants
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.model.orderdata.ongoingorderlist.Order
import com.greypixstudio.broovisdeliveryapp.ui.activity.NotDeliveredReasonActivity
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseFragment
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Event
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.viewmodel.orderdetail.OrderDetailViewModel
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class OnGoingOrdersListFragment : BaseFragment(), OnGoingOrderListAdapter.OnItemClickListener {


    private lateinit var binding: OnGoingOrdersListFragmentBinding
    private lateinit var mOnGoingOrderListAdapter: OnGoingOrderListAdapter
    private val upcomingOrderList = ArrayList<Order>()
    private val orderDetailViewModel by viewModel<OrderDetailViewModel>()

    private var completedDeliveryCount: Int = 0
    private var totalDeliveryCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_on_going_orders_list,
            container,
            false
        )
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getOrderDetailListObserver()
    }


    private fun getOrderDetailListObserver() {
        if (Utils.checkConnection(requireActivity())) {
            orderDetailViewModel.getOrderListDetailResponse(toString())
        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (!orderDetailViewModel.getOrderListDetailData.hasObservers()) {
            orderDetailViewModel.getOrderListDetailData.observe(requireActivity()) { getOrderListDetailData ->
                if (getOrderListDetailData.success) {
                    Utils.hideProgress()
                    upcomingOrderList.clear()
                    if (getOrderListDetailData.results.orders.size != 0) {
                        upcomingOrderList.addAll(getOrderListDetailData.results.orders)

                        mOnGoingOrderListAdapter =
                            OnGoingOrderListAdapter(requireActivity(), upcomingOrderList, this)
                        binding.orderListRecyclerView.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.orderListRecyclerView.setHasFixedSize(true)
                        binding.orderListRecyclerView.adapter = mOnGoingOrderListAdapter
                        binding.orderListRecyclerView.visibility = View.VISIBLE
                        binding.emptyCartImgView.visibility = View.GONE
                    } else {
                        binding.orderListRecyclerView.visibility = View.GONE
                        binding.emptyCartImgView.visibility = View.VISIBLE
                    }

                    completedDeliveryCount =
                        getOrderListDetailData.results.orderTotals.deliveredOrders
                    totalDeliveryCount = getOrderListDetailData.results.orderTotals.totalOrders


                } else {
                    binding.orderListRecyclerView.visibility = View.GONE
                    binding.emptyCartImgView.visibility = View.VISIBLE
                }
            }
        }
        /**
         * observe for failed response from API
         */
        if (!orderDetailViewModel.loadingState.hasObservers()) {
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
                                if (upcomingOrderList.size == 0) {
                                    binding.orderListRecyclerView.visibility = View.GONE
                                    binding.emptyCartImgView.visibility = View.VISIBLE
                                } else {
                                    getOrderDetailListObserver()
                                    Toast.makeText(
                                        requireActivity(),
                                        errorData.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
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
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deliveredOrderListCountHandledLivaData.observe(viewLifecycleOwner) {
            if (it.getContentIfNotHandled() == true) {
                getOrderDetailListObserver()
                //  orderDeliveredObserver(onGoingOrderList.type, onGoingOrderList.orderNumber)

            }
        }

    }

    override fun onDeliveredClick(onGoingOrderList: Order, position: Int) {
        if (onGoingOrderList.paymentFlag == "1") {
            val bundle = Bundle()
            bundle.putString(Constants.ORDER_TYPE, onGoingOrderList.type)
            bundle.putString(Constants.ORDER_NUMBER, onGoingOrderList.orderNumber)
            bundle.putString(Constants.CASH_PAYMENT, onGoingOrderList.cashPayment)
            val paymentQRDialogFragment = PaymentQRDialogFragment()
            paymentQRDialogFragment.arguments = bundle
            paymentQRDialogFragment.show(
                requireActivity().supportFragmentManager,
                "paymentQRDialogFragment"
            )
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.sub_delivered_order)
            builder.setMessage(R.string.delivered_order_desc)
            builder.setPositiveButton(getString(R.string.lbl_confirm)) { dialogInterface, _ ->
                dialogInterface.dismiss()
                orderDeliveredObserver(onGoingOrderList.type, onGoingOrderList.orderNumber)

            }
            builder.setNegativeButton(getString(R.string.lbl_cancel)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    private fun orderDeliveredObserver(orderType: String, orderNumber: String) {

        val jsonObject = JSONObject()

        jsonObject.put(JsonConstants.type, orderType)
        jsonObject.put(JsonConstants.orderNumber, orderNumber)

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
                } else {
                    getOrderDetailListObserver()
                    Toast.makeText(
                        requireActivity(),
                        orderDeliveredData.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
        /**
         * observe for failed response from API
         */
        if (!orderDetailViewModel.loadingState.hasObservers()) {
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
                        getOrderDetailListObserver()
                        if (errorData!!.message != null) {
                            Toast.makeText(
                                requireActivity(),
                                errorData!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        errorData.let {
                            getOrderDetailListObserver()
                            val errorCode = errorData?.errorCode
                            val errorMessage = errorData?.message
                            if (errorCode == 200) {
                                if (upcomingOrderList.size == 0) {
                                    binding.orderListRecyclerView.visibility = View.GONE
                                    binding.emptyCartImgView.visibility = View.VISIBLE
                                } else {
                                    getOrderDetailListObserver()
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
        mIntent.putExtra(JsonConstants.type, onGoingOrderList.type)
        mIntent.putExtra(JsonConstants.orderNumber, onGoingOrderList.orderNumber)
        startActivity(mIntent)
    }

    override fun onCallImageClick(onGoingOrderList: Order, position: Int) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + onGoingOrderList.customerMobile)
        startActivity(dialIntent)
    }

    companion object {
        var deliveredOrderListCountHandledLivaData: MutableLiveData<Event<Boolean>> =
            MutableLiveData<Event<Boolean>>()
    }
}




