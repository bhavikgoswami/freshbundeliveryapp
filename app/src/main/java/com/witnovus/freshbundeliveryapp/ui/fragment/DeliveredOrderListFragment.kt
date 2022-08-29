package com.witnovus.freshbundeliveryapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.adapter.DeliveredOrderListAdapter
import com.witnovus.freshbundeliveryapp.databinding.DeliveredOrderListFragmentBinding
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.model.orderdata.deliveredorderlist.Record
import com.witnovus.freshbundeliveryapp.ui.base.BaseFragment
import com.witnovus.freshbundeliveryapp.utils.Constants
import com.witnovus.freshbundeliveryapp.utils.Utils
import com.witnovus.freshbundeliveryapp.viewmodel.orderdetail.OrderDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList


class DeliveredOrderListFragment : BaseFragment(), DeliveredOrderListAdapter.OnItemClickListener {

    private lateinit var binding: DeliveredOrderListFragmentBinding
    private lateinit var mDeliveredOrderListAdapter: DeliveredOrderListAdapter
    private val deliveredOrderList = ArrayList<Record>()
    private val orderDetailViewModel by viewModel<OrderDetailViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_delivered_order_list,
            container,
            false
        )
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        deliveredOrderListObserver()
    }

    private fun deliveredOrderListObserver() {
        if (Utils.checkConnection(requireActivity())) {

            orderDetailViewModel.deliveredOrderListResponse(toString())

        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (!orderDetailViewModel.getdeliveredOrderListData.hasObservers()) {
            orderDetailViewModel.getdeliveredOrderListData.observe(requireActivity()) { getdeliveredOrderListData ->
                if (getdeliveredOrderListData.success) {
                    Utils.hideProgress()
                    deliveredOrderList.clear()
                    if (getdeliveredOrderListData.results.records.size !=0) {
                        deliveredOrderList.addAll(getdeliveredOrderListData.results.records)
                        deliveredOrderList.reverse()
                        mDeliveredOrderListAdapter =
                            DeliveredOrderListAdapter(requireActivity(), deliveredOrderList, this)
                        binding.deliveredOrderListRecyclerView.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.deliveredOrderListRecyclerView.setHasFixedSize(true)
                        binding.deliveredOrderListRecyclerView.adapter = mDeliveredOrderListAdapter

                        binding.deliveredOrderListRecyclerView.visibility = View.VISIBLE
                        binding.emptyCartImgView.visibility = View.GONE
                    } else {
                        binding.deliveredOrderListRecyclerView.visibility = View.GONE
                        binding.emptyCartImgView.visibility = View.VISIBLE
                    }
                } else {
                    binding.deliveredOrderListRecyclerView.visibility = View.GONE
                    binding.emptyCartImgView.visibility = View.VISIBLE
                }
            }
        }
        /**
         * observe for failed response from API
         */
        if (!orderDetailViewModel.loadingState.hasObservers()) {
            orderDetailViewModel.loadingState.observe(
                requireActivity(),
                androidx.lifecycle.Observer { loadingState ->
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
                            errorData.let {
                                val errorCode = errorData?.errorCode
                                val errorMessage = errorData?.message
                                if (errorCode == 200) {
                                    binding.deliveredOrderListRecyclerView.visibility = View.GONE
                                    binding.emptyCartImgView.visibility = View.VISIBLE
                                } else {
                                    checkErrorCode(errorCode!!)
                                    Toast.makeText(
                                        requireActivity(),
                                        errorData!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        }
                    }
                })
        }
    }

    override fun onVacationClick(deliveredOrderList: Record, position: Int) {
        val bundle = Bundle()
        bundle.putString(Constants.SUBSCRIPTION_ORDER_ID, deliveredOrderList.orderId.toString())
        val vacationBottomSheetFragment = VacationModeBottomSheetDialogFragment()
        vacationBottomSheetFragment.arguments = bundle
        vacationBottomSheetFragment.show(
            requireActivity().supportFragmentManager,
            "vacationModeDialog"
        )
    }


}