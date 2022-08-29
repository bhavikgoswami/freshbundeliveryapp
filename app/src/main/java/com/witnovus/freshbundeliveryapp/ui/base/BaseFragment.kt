package com.witnovus.freshbundeliveryapp.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.ui.fragment.InvalidUserBottomSheetFragment
import com.witnovus.freshbundeliveryapp.utils.Constants

open class BaseFragment: Fragment() {

    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
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
           requireActivity(). supportFragmentManager,
            "invalidUserBottomSheetFragment"
        )
    }

}