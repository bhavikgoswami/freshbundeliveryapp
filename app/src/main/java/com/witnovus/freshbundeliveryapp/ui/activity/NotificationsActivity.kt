package com.witnovus.freshbundeliveryapp.ui.activity

import android.app.Notification
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.witnovus.freshbundeliveryapp.viewmodel.notification.NotificationViewModel
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.adapter.NotificationAdapter
import com.witnovus.freshbundeliveryapp.databinding.NotificationsActivityBinding
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.model.notification.notificationlist.Record
import com.witnovus.freshbundeliveryapp.ui.base.BaseActivity
import com.witnovus.freshbundeliveryapp.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationsActivity : BaseActivity(), NotificationAdapter.OnItemClickListener {

    private lateinit var binding: NotificationsActivityBinding
    private lateinit var mNotificationAdapter: NotificationAdapter
    private val notificationList = ArrayList<Record>()
    private val notificationViewModel by viewModel<NotificationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notifications)
        init()
    }

    private fun init() {
        mNotificationAdapter = NotificationAdapter(this, notificationList, this)
        binding.notificationListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notificationListRecyclerView.setHasFixedSize(true)
        binding.notificationListRecyclerView.adapter = mNotificationAdapter

        binding.notificationToolbar.backACImageView.setOnClickListener {
            onBackPressed()
        }
        binding.notificationToolbar.clearBtn.setOnClickListener {
            alertClearAllNotification()
        }
        getNotifications()
    }

    private fun getNotifications() {
        if (Utils.checkConnection(this@NotificationsActivity)) {
            notificationViewModel.getNotification()
        } else {
            Toast.makeText(
                this@NotificationsActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (!notificationViewModel.notificationResponse.hasObservers()) {
            notificationViewModel.notificationResponse.observe(this) { notificationResponse ->
                if (notificationResponse.success) {
                    if (notificationResponse.results != null) {
                        notificationResponse.results.records!!.let { notificationList.addAll(it) }
                        notificationList.reverse()
                        if (notificationList.size == 0) {
                            binding.notificationListRecyclerView.visibility = View.GONE
                            binding.noNotification.visibility = View.VISIBLE
                            binding.notificationToolbar.clearBtn.visibility = View.GONE
                        } else {
                            binding.notificationToolbar.clearBtn.visibility = View.VISIBLE
                            binding.noNotification.visibility = View.GONE
                            binding.notificationListRecyclerView.visibility = View.VISIBLE
                        }

                    } else {
                        mNotificationAdapter.notifyDataSetChanged()
                        binding.notificationListRecyclerView.visibility = View.GONE
                        binding.noNotification.visibility = View.VISIBLE
                        binding.notificationToolbar.clearBtn.visibility = View.GONE
                        Toast.makeText(
                            this@NotificationsActivity,
                            notificationResponse.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    binding.notificationListRecyclerView.visibility = View.GONE
                    binding.noNotification.visibility = View.VISIBLE
                    binding.notificationToolbar.clearBtn.visibility = View.GONE
                    mNotificationAdapter.notifyDataSetChanged()
                    Toast.makeText(
                        this@NotificationsActivity,
                        notificationResponse.message,
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        }

        /**
         * observe for failed response from API
         */
        if (!notificationViewModel.loadingState.hasObservers()) {
            notificationViewModel.loadingState.observe(this) { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@NotificationsActivity)
                        }
                    }
                    LoadingState.Status.SUCCESS -> {
                        Utils.hideProgress()
                    }
                    LoadingState.Status.FAILED -> {
                        Utils.hideProgress()
                        val errorData = loadingState.errorData
                        val errorCode = errorData!!.errorCode
                        binding.noNotification.visibility = View.VISIBLE
                        if (errorCode == 200) {
                            showToast(errorData.message)
                        } else {
                            checkErrorCode(errorData.errorCode)
                        }
                        binding.notificationListRecyclerView.visibility = View.GONE
                        binding.noNotification.visibility = View.VISIBLE
                        binding.notificationToolbar.clearBtn.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun alertClearAllNotification() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.lbl_clear_notification)
        builder.setMessage(R.string.lbl_clear_notification_msg)
        builder.setPositiveButton(getString(R.string.lbl_confirm)) { dialogInterface, _ ->
            dialogInterface.dismiss()
            clearNotification()
        }
        builder.setNegativeButton(getString(R.string.lbl_cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun clearNotification() {
        if (Utils.checkConnection(this@NotificationsActivity)) {
            notificationViewModel.clearNotification()
        } else {
            Toast.makeText(
                this@NotificationsActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (!notificationViewModel.notificationClearResponse.hasObservers()) {
            notificationViewModel.notificationClearResponse.observe(this) { notificationResponse ->
                if (notificationResponse.success) {
                    notificationList.clear()
                    binding.notificationListRecyclerView.visibility = View.GONE
                    binding.noNotification.visibility = View.VISIBLE
                    binding.notificationToolbar.clearBtn.visibility = View.GONE
                    mNotificationAdapter.notifyDataSetChanged()
                    Toast.makeText(
                        this@NotificationsActivity,
                        notificationResponse.message,
                        Toast.LENGTH_LONG
                    ).show()
                    getNotifications()

                } else
                    Toast.makeText(
                        this@NotificationsActivity,
                        notificationResponse.message,
                        Toast.LENGTH_LONG
                    ).show()
                mNotificationAdapter.notifyDataSetChanged()

            }
        }

        /**
         * observe for failed response from API
         */
        if (!notificationViewModel.loadingState.hasObservers()) {
            notificationViewModel.loadingState.observe(this) { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@NotificationsActivity)
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
                            checkErrorCode(errorData.errorCode)
                        }
                    }
                }
            }
        }
    }

    override fun onNotificationClick(item: Record, position: Int) {
        TODO("Not yet implemented")
    }
}
