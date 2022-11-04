package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.OtpActivityBinding
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.viewmodel.user.UserViewModel
import io.paperdb.Paper
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class OTPActivity : BaseActivity() {

    private var type: Int = 0
    private var mobile: String = ""
    private lateinit var binding: OtpActivityBinding
    private val userViewModel by viewModel<UserViewModel>()
    private var mTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpactivity)
        viewInit()
        init()

        if (Paper.book().contains(Constants.USER_TOKEN)) {
            Paper.book().delete(Constants.USER_TOKEN)
        }
        if (Paper.book().contains(Constants.USER_DETAILS)) {
            Paper.book().delete(Constants.USER_DETAILS)
        }
    }

    private fun init() {
        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 0)
            mobile = intent.getStringExtra("mobile").toString()
        }
        startTimer()


        binding.resendACTxtView.setOnClickListener {
            setResendObserver()
            if (Utils.checkConnection(this@OTPActivity)) {
                userViewModel.generateOTP(mobile)
            } else {
                Toast.makeText(
                    this@OTPActivity,
                    getString(R.string.msg_internet_connection_not_available),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        binding.verifyOTPBtn.setOnClickListener {
            val otpValue = binding.otpPinView.text.toString()
            println("otpValue: $otpValue")
            if (otpValue.length != 4) {
                Toast.makeText(
                    this@OTPActivity,
                    "Please enter valid OTP",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                setObserver()
                userViewModel.verifyOTP(mobile, otpValue, type)
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

    /**
     * This function starts the timer for OTP expiry time
     */
    private fun startTimer() {
        if (mTimer != null) {
            mTimer?.cancel()
            mTimer = null
        }
        mTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                try {
                    val sec: Long = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                    binding.apply {
                        timerTxtView.text =
                            sec.toString().plus(" ").plus(getString(R.string.lbl_seconds))
                        timerTxtView.visibility = View.VISIBLE
                        timerPrefTxtView.visibility = View.VISIBLE
                        resendACTxtView.text = getString(R.string.lbl_resend_in)
                    }

                } catch (e: Exception) {
                }
            }

            override fun onFinish() {
                cancelTimer()
                binding.apply {
                    timerTxtView.visibility = View.GONE
                    timerPrefTxtView.visibility = View.GONE
                    resendACTxtView.text = getString(R.string.lbl_resend_otp)
                }
            }
        }
        mTimer?.start()
    }

    /**
     * This function cancel the timer run for OTP expiry time
     */
    private fun cancelTimer() {
        try {
            if (mTimer != null) {
                mTimer?.cancel()
            }
        } catch (e: Exception) {
        }
    }

    private fun setResendObserver() {
        /**
         * observe for success response from Generate OTP API
         */
        if (!userViewModel.generateOTPData.hasObservers()) {
            userViewModel.generateOTPData.observe(
                this,
                Observer { OTPResponse ->
                    if (OTPResponse != null) {
                        if (OTPResponse.success) {
                            Utils.hideProgress()
                            Toast.makeText(
                                this@OTPActivity,
                                getString(R.string.lbl_send_otp),
                                Toast.LENGTH_SHORT
                            ).show()
                            startTimer()
                        } else {
                            Toast.makeText(
                                this@OTPActivity,
                                OTPResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })

        }

        /**
         * observe for failed response from Generate OTP API
         */
        if (!userViewModel.loadingState.hasObservers()) {
            userViewModel.loadingState.observe(this, Observer { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@OTPActivity)
                        }
                    }
                    LoadingState.Status.SUCCESS -> {
                        Utils.hideProgress()
                    }
                    LoadingState.Status.FAILED -> {
                        Utils.hideProgress()
                        val errorData = loadingState.errorData
                        val errorCode = errorData!!.errorCode
                        if (errorCode == 403) {
                            Toast.makeText(
                                this,
                                "Your OTP is expired.Please try again",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            checkErrorCode(errorCode!!)
                        }

                    }
                }
            })
        }
    }

    private fun setObserver() {
        /**
         * observe for success response from Generate OTP API
         */
        if (!userViewModel.verifyOTPData.hasObservers()) {
            userViewModel.verifyOTPData.observe(
                this,
                Observer { verifyOTP ->
                    if (verifyOTP.success) {
                        lateinit var mIntent: Intent
                        if (type == 0) {
                            Toast.makeText(
                                this@OTPActivity,
                                verifyOTP.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            mIntent = Intent(this@OTPActivity, ProfileActivity::class.java)
                            mIntent.putExtra("register", true)
                            mIntent.putExtra("mobile", mobile)

                        }
                        if (type == 1) {
                            Toast.makeText(
                                this@OTPActivity,
                                verifyOTP.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            mIntent = Intent(this@OTPActivity, MPINActivity::class.java)
                            mIntent.putExtra("register", true)
                            mIntent.putExtra("mobile", mobile)

                            Paper.book().write(Constants.USER_TOKEN, verifyOTP.results.token)
                            Paper.book().write(Constants.USER_DETAILS, verifyOTP.results.user)


                        }
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(mIntent)
                        overridePendingTransition(0, 0)
                        finish()
                    } else {
                        Toast.makeText(this@OTPActivity, verifyOTP.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                })

        }

        /**
         * observe for failed response from Generate OTP API
         */
        if (!userViewModel.loadingState.hasObservers()) {
            userViewModel.loadingState.observe(this, Observer { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@OTPActivity)
                        }
                    }
                    LoadingState.Status.SUCCESS -> {
                        Utils.hideProgress()
                    }
                    LoadingState.Status.FAILED -> {
                        Utils.hideProgress()
                        val errorData = loadingState.errorData
                        val errorCode = errorData!!.errorCode
                    }
                }
            })
        }
    }
}