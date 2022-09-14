package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.MobileNumberActivityBinding
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.viewmodel.user.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MobileNumberActivity : BaseActivity() {

    private lateinit var binding: MobileNumberActivityBinding
    private val userViewModel by viewModel<UserViewModel>()

    private var mobileNo: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mobile_number)
        init()
    }

    private fun init() {
        binding.sendACBtn.setOnClickListener {
            mobileNo = binding.signInFragPhoneEdt.text.toString()
            if (mobileNo?.trim()!!.isNotEmpty()) {
                if (Utils.verifyPhoneNumber(mobileNo!!)) {
                    setObserver()
                    if (Utils.checkConnection(this@MobileNumberActivity)) {
                        userViewModel.generateOTP(mobileNo!!)
                    } else {
                        Toast.makeText(
                            this@MobileNumberActivity,
                            getString(R.string.msg_internet_connection_not_available),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MobileNumberActivity,
                        getString(R.string.msg_please_enter_valid_mobile_number),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@MobileNumberActivity,
                    getString(R.string.msg_please_enter_mbile_number),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val fullText = getString(R.string.lbl_term_privacy)
        val wordToSpan: Spannable = SpannableString(fullText)
        val cs: ClickableSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                moveToTermsAndConditionActivity()
            }
        }

        val secCs: ClickableSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                movePrivacyPolicyActivity()
            }
        }

        wordToSpan.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this@MobileNumberActivity,
                    R.color.color_theme
                )
            ),
            27,
            39,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        wordToSpan.setSpan(cs, 27, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        wordToSpan.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this@MobileNumberActivity,
                    R.color.color_theme
                )
            ),
            44,
            fullText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        wordToSpan.setSpan(secCs, 44, fullText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.termAndPrivacyTxtView.setText(wordToSpan, TextView.BufferType.SPANNABLE)
        binding.termAndPrivacyTxtView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun moveToTermsAndConditionActivity() {
        val activityIntent: Intent = Intent(this@MobileNumberActivity, WebViewActivity::class.java)
        activityIntent.putExtra(Constants.WEB_VIEW_URL, Constants.TERMS_AND_CONDITION_URL)
        activityIntent.putExtra(Constants.SCREEN_TITLE, getString(R.string.lbl_terms_of_service))
        startActivity(activityIntent)
        overridePendingTransition(0, 0)
    }

    private fun movePrivacyPolicyActivity() {
        val activityIntent: Intent = Intent(this@MobileNumberActivity, WebViewActivity::class.java)
        activityIntent.putExtra(Constants.WEB_VIEW_URL, Constants.PRIVACY_POLICY_URL)
        activityIntent.putExtra(Constants.SCREEN_TITLE, getString(R.string.lbl_privacy_policy))
        startActivity(activityIntent)
        overridePendingTransition(0, 0)
    }

    fun setObserver() {
        /**
         * observe for success response from Generate OTP API
         */
        if (!userViewModel.generateOTPData.hasObservers()) {
            userViewModel.generateOTPData.observe(
                this,
                Observer { OTPResponse ->
                    if (OTPResponse.success) {
                        Utils.hideProgress()
                        Toast.makeText(
                            this@MobileNumberActivity,
                            getString(R.string.lbl_send_otp),
                            Toast.LENGTH_SHORT
                        ).show()
                        val mIntent = Intent(this@MobileNumberActivity, OTPActivity::class.java)
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        mIntent.putExtra("type", OTPResponse.results.data.type)
                        mIntent.putExtra("mobile", mobileNo)
                        startActivity(mIntent)
                        overridePendingTransition(0, 0)
                        finish()
                    } else {
                        Toast.makeText(
                            this@MobileNumberActivity,
                            OTPResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
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
                            Utils.showProgress(this@MobileNumberActivity)
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
                            showToast(errorData.message)
                        } else {
                            checkErrorCode(errorCode!!)
                        }
                    }
                }
            })
        }
    }
}