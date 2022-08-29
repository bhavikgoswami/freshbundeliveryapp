package com.witnovus.freshbundeliveryapp.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.savvi.rangedatepicker.CalendarPickerView
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.VacationModeBottomSheetDialogFragmentBinding
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.ui.activity.VerifyVacationOTPActivity
import com.witnovus.freshbundeliveryapp.utils.Constants
import com.witnovus.freshbundeliveryapp.utils.DateUtils
import com.witnovus.freshbundeliveryapp.utils.Utils
import com.witnovus.freshbundeliveryapp.viewmodel.orderdetail.OrderDetailViewModel
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [VacationModeBottomSheetDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VacationModeBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: VacationModeBottomSheetDialogFragmentBinding
    private val orderDetailViewModel by viewModel<OrderDetailViewModel>()

    var subscriptionOrderId = ""
    var startDate = ""
    var endDate = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_vacation_mode_bottom_sheet_dialog,
            container,
            false
        )

        subscriptionOrderId = arguments?.get(Constants.SUBSCRIPTION_ORDER_ID).toString()
        init()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.root.layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT


    }

    @SuppressLint("NewApi")
    fun init() {
        binding.closeACImgView.setOnClickListener {
            this.dismiss()
        }

        setupCalendar()
        binding.applyButton.setOnClickListener {
            val dateList = ArrayList<String>()
            for (i in binding.calendarView.selectedDates) {
                DateUtils.getFormattedDatetwo(i)
                dateList.add(DateUtils.getFormattedDatetwo(i))
            }
            if (dateList.size != 0) {
                startDate = dateList.get(0)
                endDate = dateList.get(dateList.size - 1)
                Log.e("Dates", startDate + "enddate" + endDate)
                setSubscriptionOrderVacationDataObserver()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msg_apply_vacation),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setSubscriptionOrderVacationDataObserver() {
        val paramObject = JSONObject()

        paramObject.put("subscription_order_id", subscriptionOrderId)
        paramObject.put("start_date", startDate)
        paramObject.put("end_date", endDate)

        if (Utils.checkConnection(requireActivity())) {
            orderDetailViewModel.vacationApply(paramObject.toString())
        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }



        if (!orderDetailViewModel.vacationApplyData.hasObservers()) {
            orderDetailViewModel.vacationApplyData.observe(this) { vacationApplyData ->
                if (vacationApplyData.success) {
                    Utils.hideProgress()
                    Toast.makeText(
                        requireActivity(),
                        vacationApplyData.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    val mIntent = Intent(requireContext(), VerifyVacationOTPActivity::class.java)
                    mIntent.putExtra("subscription_order_id", subscriptionOrderId)
                    mIntent.putExtra("start_date", startDate)
                    mIntent.putExtra("end_date", endDate)
                    startActivity(mIntent)
                    this.dismiss()
                }


            }
        }
        /**
         * observe for failed response from API
         */
        if (!orderDetailViewModel.loadingState.hasObservers()) {
            orderDetailViewModel.loadingState.observe(this) { loadingState ->
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
                        val errorCode = errorData!!.errorCode
                        if (errorCode == 200) {
                            Toast.makeText(
                                requireActivity(),
                                errorData.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                            checkErrorCode(errorCode)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private fun setupCalendar() {
        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.DAY_OF_MONTH, 60)

        val lastYear = Calendar.getInstance()
        lastYear.add(Calendar.DATE, +1)

        val list: ArrayList<Int> = ArrayList()

        binding.calendarView.deactivateDates(list)

        binding.calendarView.init(
            lastYear.time,
            nextYear.time,
            SimpleDateFormat("MMMM, YYYY", Locale.getDefault())
        )
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withDeactivateDates(list)
        //            .withHighlightedDates(arrayList)

        binding.calendarView.scrollToDate(Date())
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment VacationModeBottomSheetDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = VacationModeBottomSheetDialogFragment().apply {}
    }

    fun checkErrorCode(errorCode: Int) {
        if (errorCode == Constants.INVALID_TOKEN_ERROR) {
            openDialog(getString(R.string.lbl_invalid_token))
        } else if (errorCode == Constants.TOKEN_EXPIRED_ERROR) {
            openDialog(getString(R.string.lbl_expired_token))
        } else if (errorCode == Constants.ACCOUNT_DEACTIVATED_ERROR) {
            openDialog(getString(R.string.lbl_deactive_user))
        } else if (errorCode == Constants.CUSTOM_EXCEPTION_ERROR_CODE) {
            showToast(getString(R.string.msg_something_went_wrong))
        }
    }

    fun openDialog(message: String) {
        val bundle = Bundle()
        bundle.putString(Constants.ERROR_MESSAGE, message)
        val invalidUserBottomSheetFragment = InvalidUserBottomSheetFragment()
        invalidUserBottomSheetFragment.arguments = bundle
        invalidUserBottomSheetFragment.isCancelable = false;
        invalidUserBottomSheetFragment.show(
            requireActivity().supportFragmentManager,
            "invalidUserBottomSheetFragment"
        )
    }

    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}