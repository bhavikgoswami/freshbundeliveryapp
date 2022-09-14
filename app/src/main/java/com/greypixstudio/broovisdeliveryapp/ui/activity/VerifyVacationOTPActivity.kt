package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.VerifyVacationOTPActivityBinding
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.viewmodel.orderdetail.OrderDetailViewModel
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_otpactivity.*
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerifyVacationOTPActivity : BaseActivity() {

    private lateinit var binding: VerifyVacationOTPActivityBinding
    private val orderDetailViewModel by viewModel<OrderDetailViewModel>()

    private var subscriptionsOrderId: String = ""
    private var startDate: String = ""
    private var endDate: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_verify_vacation_otpactivity)
        viewInit()
        init()
    }


    private fun init() {
        if (intent.hasExtra(Constants.SUBSCRIPTION_ORDER_ID_VERIFY_VACATION_OTP)) {
            subscriptionsOrderId =
                intent.getStringExtra(Constants.SUBSCRIPTION_ORDER_ID_VERIFY_VACATION_OTP)
                    .toString()
            startDate = intent.getStringExtra(Constants.VACATION_APPLY_START_DATE).toString()
            endDate = intent.getStringExtra(Constants.VACATION_APPLY_END_DATE).toString()
        } else {
            finish()
        }

        verifyOTPBtn.setOnClickListener {
            val otp = otpPinView.text.toString()
            println("otpValue: $otp")
            if (otp.length != 4) {
                Toast.makeText(
                    this@VerifyVacationOTPActivity,
                    "Please enter valid OTP",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                verifyVacationOtpObserver()

            }
        }
    }

    private fun viewInit() {
        binding.otpPinView.setAnimationEnable(true)
        binding.otpPinView.itemCount = 4
        binding.otpPinView.cursorColor =
            ResourcesCompat.getColor(resources, R.color.black, theme)
        binding.otpPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d(
                    "TAG",
                    "onTextChanged() called with: s = [$s], start = [$start], before = [$before], count = [$count]"
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun verifyVacationOtpObserver() {

        val paramObject = JSONObject()

        val otp = otpPinView.text.toString()

        paramObject.put("subscription_order_id", subscriptionsOrderId)
        paramObject.put("start_date", startDate)
        paramObject.put("end_date", endDate)
        paramObject.put("otp", otp)

        if (Utils.checkConnection(this@VerifyVacationOTPActivity)) {
            orderDetailViewModel.vacationVerify(paramObject.toString())
        } else {
            Toast.makeText(
                this@VerifyVacationOTPActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }



        if (!orderDetailViewModel.vacationVerifyData.hasObservers()) {
            orderDetailViewModel.vacationVerifyData.observe(
                this, Observer { vacationVerifyData ->
                    if (vacationVerifyData.success) {
                        finish()
                        Toast.makeText(
                            this@VerifyVacationOTPActivity,
                            vacationVerifyData.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })

        }

        /**
         * observe for failed response from Generate OTP API
         */
        if (!orderDetailViewModel.loadingState.hasObservers()) {
            orderDetailViewModel.loadingState.observe(this, Observer { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@VerifyVacationOTPActivity)
                        }
                    }
                    LoadingState.Status.SUCCESS -> {
                        Utils.hideProgress()
                    }
                    LoadingState.Status.FAILED -> {
                        Utils.hideProgress()
                        val errorData = loadingState.errorData
                        val errorCode = errorData!!.errorCode
                        checkErrorCode(errorCode!!)
                    }
                }
            })
        }
    }
}