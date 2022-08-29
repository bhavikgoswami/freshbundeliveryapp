package com.witnovus.freshbundeliveryapp.ui.fragment


import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.witnovus.nepzep.utils.Event
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.adapter.ViewPagerAdapter
import com.witnovus.freshbundeliveryapp.databinding.HomeFragmentBinding
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.ui.base.BaseFragment
import com.witnovus.freshbundeliveryapp.utils.Utils
import com.witnovus.freshbundeliveryapp.utils.imagepicker.utils.PickerOptions.Companion.init
import com.witnovus.freshbundeliveryapp.viewmodel.orderdetail.OrderDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment() {

    private lateinit var binding: HomeFragmentBinding
    private val orderDetailViewModel by viewModel<OrderDetailViewModel>()


    private var completedDeliveryCount: Int = 0
    private var totalDeliveryCount: Int = 0
    private var cancelDeliveryCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        getOrderDetailListObserver()
        initTabViews()
        val simpleDateFormat = SimpleDateFormat("EEEE, dd  MMMM")
        val currentDate: String = simpleDateFormat.format(Date())
        binding.todayDateTxtView.text = currentDate
        init()
        return binding.root
    }


    companion object {
        var deliveredOrderListCountHandledLivaData: MutableLiveData<Event<Boolean>> =
            MutableLiveData<Event<Boolean>>()

        @JvmStatic
        fun newInstance() = HomeFragment().apply {}
    }

    fun init() {
        deliveredOrderListCountHandledLivaData.observe(viewLifecycleOwner) {
            if (it.getContentIfNotHandled() == true) {
                println(">>>>>>>>>>>>>>>>> bottomsheet closed")
                getOrderDetailListObserver()
            }
        }
    }


    private fun getOrderDetailListObserver() {
        if (Utils.checkConnection(requireActivity())) {

            orderDetailViewModel.getOrderListDetailResponse(toString())

        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (!orderDetailViewModel.getOrderListDetailData.hasObservers()) {
            orderDetailViewModel.getOrderListDetailData.observe(requireActivity()) { getOrderListDetailData ->
                if (getOrderListDetailData.success) {
                    Utils.hideProgress()

                    binding.numberOfOrderDeliveredTxtView.text =
                        getOrderListDetailData.results.orderTotals.totalOrders.toString()
                    binding.newSubscriberOrderNumberTxtView.text =
                        getOrderListDetailData.results.orderTotals.newSubscribers.toString()
                    binding.totalDeliveryOrderTxtView.text =
                        getOrderListDetailData.results.orderTotals.deliveredOrders.toString()
                    binding.totalCancelDeliveryNumberTxtView.text =
                        getOrderListDetailData.results.orderTotals.notDeliveredOrders.toString()


                    completedDeliveryCount =
                        getOrderListDetailData.results.orderTotals.deliveredOrders
                    totalDeliveryCount = getOrderListDetailData.results.orderTotals.totalOrders
                    cancelDeliveryCount =
                        getOrderListDetailData.results.orderTotals.notDeliveredOrders

                    if (totalDeliveryCount != 0) {
                        manageProgressBar(
                            totalDeliveryCount,
                            completedDeliveryCount,
                            cancelDeliveryCount
                        )
                    }
                }
            }
        }
        /**
         * observe for failed response from API
         */
        if (!orderDetailViewModel.loadingState.hasObservers()) {
            orderDetailViewModel.loadingState.observe(requireActivity(), Observer { loadingState ->
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
                                Toast.makeText(
                                    requireActivity(),
                                    errorData!!.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                checkErrorCode(errorCode!!)
                            }
                        }

                    }
                }
            })
        }
    }


    private fun manageProgressBar(
        totalDeliveryCount: Int,
        completedDeliveryCount: Int,
        cancelDeliveryCount: Int
    ) {
        binding.orderProgressSeekBar.isEnabled = false
        binding.orderProgressSeekBar.max = 100
        binding.orderProgressSeekBar.progress =
            (cancelDeliveryCount.plus(completedDeliveryCount) * 100) / totalDeliveryCount

        val deliveryCount = completedDeliveryCount + cancelDeliveryCount
        binding.deliveredNoTxtVnumber.text =
            deliveryCount.toString().plus("/").plus(totalDeliveryCount)
    }

    private fun initTabViews() {

        binding.tabs.addTab(
            binding.tabs.newTab().setText(getString(R.string.lbl_on_going_order))
                .setTag(getString(R.string.lbl_on_going_order))
        )
        binding.tabs.addTab(
            binding.tabs.newTab().setText(getString(R.string.lbl_delivered))
                .setTag(getString(R.string.lbl_delivered))
        )
        binding.tabs.tabGravity = TabLayout.GRAVITY_FILL
        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                binding.tabs.selectTab(binding.tabs.getTabAt(position))

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

        })


        // set the adapter (use a new section pages adapter)
        val viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position

                // set style
                val view = tab?.customView
                if (view is AppCompatTextView) {
                    val textTypeFace = ResourcesCompat.getFont(
                        requireContext(),
                        R.font.opensansregular
                    )
                    view.setTypeface(textTypeFace, Typeface.BOLD)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val view = tab?.customView
                if (view is AppCompatTextView) {
                    val textTypeFace = ResourcesCompat.getFont(
                        requireContext(),
                        R.font.opensansregular
                    )
                    view.typeface = textTypeFace
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })


        binding.tabs.getTabAt(0)?.select()
    }

}