package com.greypixstudio.broovisdeliveryapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.adapter.DeliveredOrderListAdapter
import com.greypixstudio.broovisdeliveryapp.databinding.DeliveredOrderListFragmentBinding
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.model.orderdata.deliveredorderlist.Record
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseFragment
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.viewmodel.orderdetail.OrderDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


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