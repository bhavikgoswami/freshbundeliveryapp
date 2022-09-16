package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.VerifyMpinActivityBinding
import com.greypixstudio.broovisdeliveryapp.model.auth.user.User

import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.utils.Utils.Companion.showToast
import com.greypixstudio.broovisdeliveryapp.viewmodel.user.UserViewModel
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_mpinactivity.*
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerifyMpinActivity : BaseActivity() {

    private lateinit var binding: VerifyMpinActivityBinding
    private val userViewModel by viewModel<UserViewModel>()
    var mobileNumber = Constants.BLANK_STRING

    var verifyMpinSelectedObject: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_mpin)
        init()
        getFirebaseToken()
    }

    private fun getFirebaseToken() {

        FirebaseOptions.Builder()
            .setProjectId(Constants.PROJECT_ID)
            .setApplicationId(Constants.APPLICATION_ID)
            .setApiKey(Constants.API_KEY)
            .build()
        Firebase.initialize(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val firebaseToken = task.result

            if (Paper.book().contains(Constants.USER_TOKEN)) {
                setTokenUpdateObserver()
                userViewModel.updateFirebaseToken(firebaseToken)
            }
        })
    }

    private fun setTokenUpdateObserver() {
        if (Utils.checkConnection(this@VerifyMpinActivity)) {
            /**
             * observe for success response from update token
             */
            if (!userViewModel.signUpUserData.hasObservers()) {
                userViewModel.signUpUserData.observe(
                    this,
                    Observer { signupUser ->
                        if (signupUser != null) {
                            if (signupUser.success) {
                                Utils.hideProgress()
                            } else {
                                Toast.makeText(
                                    this@VerifyMpinActivity,
                                    signupUser.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
        } else {
            Toast.makeText(
                this@VerifyMpinActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun init() {
        binding.mPinView.setAnimationEnable(true)
        binding.mPinView.itemCount = 4
        binding.mPinView.cursorColor = ResourcesCompat.getColor(resources, R.color.black, theme)
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

        binding.verifyMpinBtn.setOnClickListener {
            if (binding.mPinView.text!!.isNotEmpty() && mPinView.text.toString().length == 4) {
                verifyMpin(mPinView.text!!.toString())
            }
        }

        binding.resetMpinTxtView.setOnClickListener {
            val mIntent = Intent(this@VerifyMpinActivity, MobileNumberActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(mIntent)
            overridePendingTransition(0, 0)
            finish()
        }
    }

    private fun verifyMpin(mpin: String) {

        val jsonObject = JSONObject()

        if (Paper.book().contains(Constants.USER_DETAILS)) {
            val userDetail = Paper.book().read<User>(Constants.USER_DETAILS)
            jsonObject.put("mobile", userDetail.primaryContactNo)
        }

        jsonObject.put(Constants.LBL_MPIN, mpin)
        if (Utils.checkConnection(this@VerifyMpinActivity)) {
            userViewModel.verifyMpinResponse(jsonObject.toString())
        } else {
            Toast.makeText(
                this@VerifyMpinActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (!userViewModel.verifyMpinData.hasObservers()) {
            userViewModel.verifyMpinData.observe(
                this@VerifyMpinActivity
            ) { verifyMpinData ->
                verifyMpinData.let {
                    if (verifyMpinData.success) {
                        Paper.book().write(Constants.USER_TOKEN, verifyMpinData.results.token)
                        Paper.book().write(Constants.USER_DETAILS, verifyMpinData.results.user)
                        if (verifyMpinData.results.user != null) {
                            Toast.makeText(
                                this@VerifyMpinActivity,
                                getString(R.string.msg_login_with_mpin),
                                Toast.LENGTH_SHORT
                            ).show()
                            lateinit var mIntent: Intent
                            if (verifyMpinData.results.user.verifiedStatus.equals("New")) {

                                mIntent =
                                    Intent(
                                        this@VerifyMpinActivity,
                                        UnderReviewProfileActivity::class.java
                                    )
                            } else if (verifyMpinData.results.user.verifiedStatus.equals("Reviewed")) {

                                mIntent =
                                    Intent(
                                        this@VerifyMpinActivity,
                                        DocumentUploadActivity::class.java
                                    )
                                mIntent.putExtra("type", 1)
                            } else if (verifyMpinData.results.user.verifiedStatus.equals("DocumentUploaded")) {
                                mIntent =
                                    Intent(
                                        this@VerifyMpinActivity,
                                        EnableLocationActivity::class.java
                                    )
                            } else if (verifyMpinData.results.user.verifiedStatus.equals("DocumentAdded")) {
                                mIntent =
                                    Intent(
                                        this@VerifyMpinActivity,
                                        EnableLocationActivity::class.java
                                    )
                            } else if (verifyMpinData.results.user.verifiedStatus.equals("AddressAdded")) {

                                mIntent =
                                    Intent(
                                        this@VerifyMpinActivity,
                                        AddBankDetailsActivity::class.java
                                    )
                            } else if (verifyMpinData.results.user.verifiedStatus.equals("BankAdded")) {
                                mIntent =
                                    Intent(
                                        this@VerifyMpinActivity,
                                        UnderReviewProfileActivity::class.java
                                    )
                            } else if (verifyMpinData.results.user.verifiedStatus.equals("Approved")) {

                                mIntent =
                                    Intent(this@VerifyMpinActivity, MainActivity::class.java)

                            } else if (verifyMpinData.results.user.verifiedStatus.equals("Reject")) {

                                mIntent =
                                    Intent(
                                        this@VerifyMpinActivity,
                                        UnderReviewProfileActivity::class.java
                                    )
                            } else {
                                mIntent = Intent(
                                    this@VerifyMpinActivity,
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
                            Utils.showProgress(this@VerifyMpinActivity)
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

