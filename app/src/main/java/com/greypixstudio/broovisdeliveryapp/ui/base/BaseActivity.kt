package com.greypixstudio.broovisdeliveryapp.ui.base

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.greypixstudio.broovisdeliveryapp.utils.Event
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.AddressData
import com.greypixstudio.broovisdeliveryapp.ui.fragment.InvalidUserBottomSheetFragment
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils.Companion.showToast
import com.greypixstudio.broovisdeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class BaseActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        )
        super.onCreate(savedInstanceState)


    }

    /**
     * This function returns the device screen width
     */
    fun getWidth(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    /**
     * This function returns the device screen height
     */
    fun getHeight(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
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
            supportFragmentManager,
            "invalidUserBottomSheetFragment"
        )
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {

        var sessionExpired: MutableLiveData<Event<Boolean>> =
            MutableLiveData<Event<Boolean>>()

        @JvmStatic
        fun newInstance() = BaseActivity().apply {}
    }

}