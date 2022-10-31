package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.MPINActivityBinding
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.viewmodel.user.UserViewModel
import io.paperdb.Paper
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_mpinactivity.*
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MPINActivity : BaseActivity() {

    private lateinit var binding: MPINActivityBinding
    private val userViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mpinactivity)
        init()
    }

    fun init() {
     //   binding.mPinView.setAnimationEnable(true)
      //  binding.mPinView.itemCount = 4
        //binding.mPinView.cursorColor =
      //      ResourcesCompat.getColor(resources, R.color.black, theme)
        binding.mPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d(
                    "TAG",
                    "onTextChanged() called with: s = [$s], start = [$start], before = [$before], count = [$count]"
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.verifyPINBtn.setOnClickListener {
            if (binding.mPinView.text!!.isNotEmpty() && mPinView.text.toString().length == 4) {

                setMpin()

            }
        }
    }

    private fun setMpin() {

        val mpin = mPinView.text.toString()

        val jsonObject = JSONObject()

        jsonObject.put("mpin", mpin)
        if (Utils.checkConnection(this@MPINActivity)) {
            userViewModel.setMpinResponse(jsonObject.toString())
        } else {
            Toast.makeText(
                this@MPINActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }



        if (!userViewModel.setMpinData.hasObservers()) {
            userViewModel.setMpinData.observe(this@MPINActivity
            ) { setMpinData ->
                setMpinData.let {
                    if (setMpinData.success) {
                        Paper.book().write(Constants.USER_DETAILS, setMpinData.results.user)
                        if (setMpinData.results.user != null) {
                            Toast.makeText(
                                this@MPINActivity,
                                getString(R.string.msg_set_mpin_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            lateinit var mIntent: Intent
                            if (setMpinData.results.user.verifiedStatus.equals("New")) {
                                mIntent =
                                    Intent(
                                        this@MPINActivity,
                                        UnderReviewProfileActivity::class.java
                                    )
                            } else if (setMpinData.results.user.verifiedStatus.equals("Reviewed")) {

                                mIntent = Intent(this@MPINActivity, DocumentUploadActivity::class.java)
                                mIntent.putExtra("type",1)
                            }else if (setMpinData.results.user.verifiedStatus.equals("DocumentUploaded")) {
                                mIntent =
                                    Intent(
                                        this@MPINActivity,
                                        EnableLocationActivity::class.java
                                    )
                            }  else if (setMpinData.results.user.verifiedStatus.equals("DocumentAdded")) {
                                mIntent =
                                    Intent(
                                        this@MPINActivity,
                                        EnableLocationActivity::class.java
                                    )
                            } else if (setMpinData.results.user.verifiedStatus.equals("AddressAdded")) {
                                mIntent =
                                    Intent(
                                        this@MPINActivity,
                                        AddBankDetailsActivity::class.java
                                    )
                            } else if (setMpinData.results.user.verifiedStatus.equals("BankAdded")) {
                                mIntent =
                                    Intent(
                                        this@MPINActivity,
                                        UnderReviewProfileActivity::class.java
                                    )
                            } else if (setMpinData.results.user.verifiedStatus.equals("Approved")) {
                                mIntent =
                                    Intent(this@MPINActivity, MainActivity::class.java)
                            } else if (setMpinData.results.user.verifiedStatus.equals("Reject")) {
                                mIntent =
                                    Intent(
                                        this@MPINActivity,
                                        UnderReviewProfileActivity::class.java
                                    )
                            } else {
                                mIntent = Intent(
                                    this@MPINActivity,
                                    UnderReviewProfileActivity::class.java
                                )
                            }
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            startActivity(mIntent)
                            overridePendingTransition(0, 0)
                            finish()
                        }


                    }
                }
            }
        }

        if (!userViewModel.loadingState.hasObservers()) {
            userViewModel.loadingState.observe(this) { loadingState ->

                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@MPINActivity)
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
            }
        }
    }
}