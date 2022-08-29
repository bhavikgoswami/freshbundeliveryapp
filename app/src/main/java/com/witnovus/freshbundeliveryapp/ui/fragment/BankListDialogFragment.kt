package com.witnovus.freshbundeliveryapp.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.witnovus.nepzep.adapter.calBottom.BankListAdapter
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.BankListActivityDialogFragmentBinding
import com.witnovus.freshbundeliveryapp.databinding.ToolbarLayoutBinding
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.model.uploaddocument.addbankdetail.banklist.Record
import com.witnovus.freshbundeliveryapp.utils.Constants
import com.witnovus.freshbundeliveryapp.utils.Utils
import com.witnovus.freshbundeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import kotlinx.android.synthetic.main.activity_bank_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class BankListDialogFragment : DialogFragment(), BankListAdapter.OnItemClicked {

    private lateinit var binding: BankListActivityDialogFragmentBinding
    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding

    private val uploadDocumentViewModel by viewModel<UploadDocumentViewModel>()
    lateinit var bankListAdapter: BankListAdapter

    private var bankList = ArrayList<Record>()
    private var selectedBankId: Int = -1
    private var selectedBankImage = ""
    private var selectedBankName = ""
    private var selectedBank: Record? = null

    private var type: Int = 5
    private var listener: BankList? = null

    interface BankList {
        fun onSelect(bank: Record?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = activity as BankList
        } catch (e: ClassCastException) {
            Log.e(
                "TAG", "onAttach: ClassCastException: "
                        + e.message
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_bank_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initRecycleView()
        getBankList()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateDialog( savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return dialog
    }

    private fun init() {
        toolbarLayoutBinding = binding.bankListToolbar
        binding.bankListToolbar.toolbarNametxtView.text = getText(R.string.title_bank_list)
        binding.bankListToolbar.backACImageView.setOnClickListener {
         dialog!!.dismiss()
        }

        binding.selectedBankApplyBtn.setOnClickListener {
            if (selectedBank != null) {
                listener!!.onSelect(selectedBank)
                dialog!!.dismiss()
            }
        }
        setBankDetail()

    }

    private fun setUpSearch() {
        binding.searchAutoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bankListAdapter.filter.filter(s)
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    private fun initRecycleView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        bankListRecyclerView.layoutManager = layoutManager
    }

    private fun getBankList() {
        if (Utils.checkConnection(requireActivity())) {
            uploadDocumentViewModel.bankListDetailResponse(toString())
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }


        if (!uploadDocumentViewModel.bankListDetailData.hasObservers()) {
            uploadDocumentViewModel.bankListDetailData.observe(
                requireActivity()
            ) { bankListDetailData ->
                bankListDetailData.let {
                    if (bankListDetailData.success) {
                        if (bankListDetailData.results.records != null) {
                            bankList.clear()
                            bankList.addAll(bankListDetailData.results.records)
                            bankListAdapter.addData(bankList)
                            bankListAdapter.notifyDataSetChanged()

                        }
                    }
                }
            }
        }

        if (!uploadDocumentViewModel.loadingState.hasObservers()) {
            uploadDocumentViewModel.loadingState.observe(this) { loadingState ->

                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(requireContext())
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

    @SuppressLint("NotifyDataSetChanged")
    private fun setBankDetail() {
        bankListAdapter = BankListAdapter(
            requireContext(), this
        )
        bankListRecyclerView.adapter = bankListAdapter
        setUpSearch()
    }

    override fun onItemClick(bank: Record?, position: Int) {
        bank!!.isSelected = true
        selectedBank = bank
        selectedBankId = bank!!.id
        selectedBankImage = bank.bankLogo!!
        selectedBankName = bank.bankName!!
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