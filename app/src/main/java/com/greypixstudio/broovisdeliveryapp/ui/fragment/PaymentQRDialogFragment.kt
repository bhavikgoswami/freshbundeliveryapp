package com.greypixstudio.broovisdeliveryapp.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.FragmentPaymentQRDialogBinding
import com.greypixstudio.broovisdeliveryapp.model.JsonConstants
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Event
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.viewmodel.orderdetail.OrderDetailViewModel
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * create an instance of this fragment.
 */
class PaymentQRDialogFragment : DialogFragment() {

    lateinit var binding: FragmentPaymentQRDialogBinding
    private val orderDetailViewModel by viewModel<OrderDetailViewModel>()
    private var orderNumber: String = ""
    private var orderType: String = ""
    private var cashPayment: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment_q_r_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            orderNumber = arguments?.getString(Constants.ORDER_NUMBER, "").toString()
            orderType = arguments?.getString(Constants.ORDER_TYPE, "").toString()
            cashPayment = arguments?.getString(Constants.CASH_PAYMENT, "").toString()
            binding.amountTextView.text =
                getString(R.string.lbl_rs) + Constants.BLANK_SPACE + cashPayment
        }
        init()
    }

    private fun init() {
        binding.cashCollectedBtn.setOnClickListener {
            showDialog(orderType, orderNumber, Constants.CASH, "")
        }
        binding.recievedUPIbtn.setOnClickListener {
            showDialog(orderType, orderNumber, Constants.ONLINE, "")
        }
    }

    private fun showDialog(
        orderType: String,
        orderNumber: String,
        paymentType: String,
        paymentID: String
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.sub_delivered_order)
        if (paymentType == Constants.CASH) {
            builder.setMessage(R.string.lbl_recieve_cash)
        } else {
            builder.setMessage(R.string.lbl_recieve_by_upi)
        }
        builder.setPositiveButton(getString(R.string.lbl_confirm)) { dialogInterface, _ ->
            dialogInterface.dismiss()
            orderDeliveredObserver(orderType, orderNumber, paymentType, paymentID)
        }
        builder.setNegativeButton(getString(R.string.lbl_cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    private fun orderDeliveredObserver(
        orderType: String,
        orderNumber: String,
        paymentType: String,
        paymentID: String
    ) {
        val jsonObject = JSONObject()
        jsonObject.put(JsonConstants.type, orderType)
        jsonObject.put(JsonConstants.orderNumber, orderNumber)
        jsonObject.put(JsonConstants.paymentType, paymentType)
        jsonObject.put(JsonConstants.paymentId, paymentID)

        orderDetailViewModel.orderDeliveredResponse(jsonObject.toString())

        if (!orderDetailViewModel.orderDeliveredData.hasObservers()) {
            orderDetailViewModel.orderDeliveredData.observe(requireActivity()) { orderDeliveredData ->
                if (orderDeliveredData.success) {
                    Utils.hideProgress()
                    // getOrderDetailListObserver()
                    (HomeFragment).deliveredOrderListCountHandledLivaData.postValue(Event(true))
                    (OnGoingOrdersListFragment).deliveredOrderListCountHandledLivaData.postValue(
                        Event(true)
                    )
                    this.dismiss()
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.msg_delivered_order),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    this.dismiss()
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
}