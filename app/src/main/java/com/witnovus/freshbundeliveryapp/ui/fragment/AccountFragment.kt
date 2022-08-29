package com.witnovus.freshbundeliveryapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.ProfileFragmentBinding
import com.witnovus.freshbundeliveryapp.model.auth.getuserdetail.Data
import com.witnovus.freshbundeliveryapp.model.auth.user.User
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.ui.activity.*
import com.witnovus.freshbundeliveryapp.ui.base.BaseFragment
import com.witnovus.freshbundeliveryapp.utils.Constants
import com.witnovus.freshbundeliveryapp.utils.Utils
import com.witnovus.freshbundeliveryapp.viewmodel.user.UserViewModel
import io.paperdb.Paper
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : BaseFragment() {

    private lateinit var binding: ProfileFragmentBinding
    private val userViewModel by viewModel<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {

        binding.profileSettingRLtLayout.setOnClickListener {
            val activityIntent = Intent(requireContext(), ProfileActivity::class.java)
            activityIntent.putExtra("type" , 1)
            startActivity(activityIntent)
        }
        binding.documentVerificationRLtLayout.setOnClickListener {
            val activityIntent = Intent(requireContext(), DocumentUploadActivity::class.java)
            activityIntent.putExtra("type",2)
            startActivity(activityIntent)
        }
        binding.addressRLtLayout.setOnClickListener {
            val activityIntent = Intent(requireContext(), GetAddressDetailActivity::class.java)
            activityIntent.putExtra("type" , 3)
            startActivity(activityIntent)
        }
        binding.bankAccRLtLayout.setOnClickListener {
            val activityIntent = Intent(requireContext(), AddBankDetailsActivity::class.java)
            activityIntent.putExtra("type" , 2)
            startActivity(activityIntent)
        }
        binding.ReferAppRLtLayout.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=${requireActivity().packageName}")
            startActivity(Intent.createChooser(intent, "Share with:"))
        }
        binding.HelpAndSupportRLtLayout.setOnClickListener {
            val activityIntent = Intent(requireContext(), HelpAndSupportActivity::class.java)
            startActivity(activityIntent)
        }
        binding.TermPolicyRLtLayout.setOnClickListener {
            val activityIntent = Intent(requireContext(), TermPrivacyPolicyActivity::class.java)
            activityIntent.putExtra(Constants.WEB_VIEW_URL, Constants.PRIVACY_POLICY_URL)
            activityIntent.putExtra(Constants.SCREEN_TITLE, getString(R.string.lbl_privacy_policy))
            startActivity(activityIntent)

        }
        binding.logoutTxtView.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setCancelable(false)
                .setMessage(getString(R.string.msg_logout))
                .setPositiveButton(getString(R.string.lbl_logout)) { dialog, which ->
                    dialog.dismiss()
                    logoutObserver()
                }.setNegativeButton(getString(R.string.lbl_cancel)) { dialog, which ->
                    dialog.dismiss()
                }.show()
        }
    }

    private fun logoutObserver() {
        if (Utils.checkConnection(requireActivity())) {
            userViewModel.logoutResponse()
        }else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }


        if (!userViewModel.logoutData.hasObservers()) {
            userViewModel.logoutData.observe(requireActivity()) { logoutData ->
                if (logoutData.success) {
                    Utils.hideProgress()
                    Paper.book().delete(Constants.USER_DETAILS)
                    Paper.book().delete(Constants.USER_TOKEN)
                    Paper.book().delete(Constants.IS_LOGIN)
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.msg_logout_successfully),
                        Toast.LENGTH_SHORT
                    ).show()

                    val mIntent = Intent(requireActivity(), MobileNumberActivity::class.java)
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(mIntent)
                    requireActivity().finish()
                }
            }
        }
        /**
         * observe for failed response from API
         */
        if (!userViewModel.loadingState.hasObservers()){
            userViewModel.loadingState.observe(requireActivity(), androidx.lifecycle.Observer { loadingState ->
                when(loadingState.status){
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
                            if (errorCode != 200) {
                                Toast.makeText(
                                    requireActivity(),
                                    errorData!!.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                checkErrorCode(errorCode)
                            }
                        }

                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        setGetUserDetailsObserver()
    }

    private fun setGetUserDetailsObserver() {
        if (Utils.checkConnection(requireContext())) {
            userViewModel.getUserDetails()
        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (!userViewModel.getUserDetailData.hasObservers()) {
            userViewModel.getUserDetailData.observe(
                this
            ) { getUserDetailData ->
                if (getUserDetailData != null) {
                    if (getUserDetailData.success) {
                        Utils.hideProgress()

                        getUserDetailData.results.data.let { records ->
                            getUserDetailData.results.data
                            setUserDetails(records)

                        }
                       /* Paper.book()
                            .write(Constants.USER_DETAILS, getUserDetailData.results.user)
                        setUserDetails()*/
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getUserDetailData.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setUserDetails(records: Data) {

            Glide.with(requireActivity())
                .load(Constants.PREFIX_DOMAIN.plus(records.image))
                .error(R.drawable.ic_thumbnail)
                .placeholder(R.drawable.ic_thumbnail)
                .into(binding.profilePicImageView1)
            binding.nameTxtView.text = records.firstName.plus(" ").plus(records.lastName)
            binding.emailTxtView.text = records.email

    }

    companion object {
        @JvmStatic
        fun newInstance() = AccountFragment().apply {}
    }
}