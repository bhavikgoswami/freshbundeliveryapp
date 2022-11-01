package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging

import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.MainActivityBinding
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.model.notification.notificationlist.Record
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.ui.fragment.HomeFragment
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Event
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.viewmodel.notification.NotificationViewModel
import com.greypixstudio.broovisdeliveryapp.viewmodel.user.UserViewModel
import io.paperdb.Paper
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : BaseActivity() {

    lateinit var binding: MainActivityBinding
    lateinit var navController: NavController
    private val userViewModel by viewModel<UserViewModel>()
    private val notificationViewModel by viewModel<NotificationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        getFirebaseToken()
        init()
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
        if (Utils.checkConnection(this@MainActivity)) {
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
                                    this@MainActivity,
                                    signupUser.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun init() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        binding.mToolBar.setBackgroundColor(getColor(R.color.black))
        binding.titleTxtView.setTextColor(getColor(R.color.white))
        binding.notificationImgView.setColorFilter(ContextCompat.getColor(this, R.color.white))
        clickEvents()
    }

    private fun clickEvents() {
        // binding.titleTxtView.text = getString(R.string.lbl_menu_home)
        binding.logoImageView.visibility = View.VISIBLE
        binding.titleTxtView.visibility = View.GONE

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_graph_home -> {
                    binding.mToolBar.setBackgroundColor(getColor(R.color.black))
                    //  binding.titleTxtView.setTextColor(getColor(R.color.white))
                    binding.notificationImgView.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        )
                    )
                    navController.navigate(R.id.nav_graph_home)
                    binding.logoImageView.visibility = View.VISIBLE
                    binding.titleTxtView.visibility = View.GONE
                    // binding.titleTxtView.text = getString(R.string.lbl_menu_home)
                }
                R.id.nav_graph_profile -> {
                    binding.mToolBar.setBackgroundColor(getColor(R.color.white))
                    binding.titleTxtView.setTextColor(getColor(R.color.black))
                    binding.notificationImgView.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.black
                        )
                    )
                    navController.navigate(R.id.nav_graph_profile)
                    binding.titleTxtView.text = getString(R.string.lbl_menu_profile)
                    binding.logoImageView.visibility = View.GONE
                    binding.titleTxtView.visibility = View.VISIBLE
                }
            }
            true
        }

        binding.notificationImgView.setOnClickListener {
            val activityIntent = Intent(this@MainActivity, NotificationsActivity::class.java)
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(activityIntent)
            overridePendingTransition(0, 0)
        }
    }

    override fun onBackPressed() {
        if (binding.bottomNav.selectedItemId == R.id.nav_graph_home) {
            finishAffinity()
        } else {
            binding.bottomNav.selectedItemId = R.id.nav_graph_home
        }
    }

    companion object {
        var notificationHandledLivaData: MutableLiveData<Event<Boolean>> =
            MutableLiveData<Event<Boolean>>()
        var notificationIcon: MutableLiveData<Event<Boolean>> =
            MutableLiveData<Event<Boolean>>()

        @JvmStatic
        fun newInstance() = MainActivity().apply {}
    }

    override fun onResume() {
        super.onResume()
        notificationIcon.observe(this) {
            if (it.getContentIfNotHandled() == true) {
                binding.notificationImgView.setImageResource(R.drawable.ic_notification)
            }
        }
        getNotifications()
    }

    private fun getNotifications() {
        if (Utils.checkConnection(this@MainActivity)) {
            notificationViewModel.getNotification()
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (!notificationViewModel.notificationResponse.hasObservers()) {
            notificationViewModel.notificationResponse.observe(this) { notificationResponse ->
                if (notificationResponse.success) {
                    if (notificationResponse.results != null) {
                        notificationResponse.results.records!!.let { notifcations ->
                            val filteredList: ArrayList<Record> = ArrayList()
                            notifcations.filter {
                                it.read_status.lowercase(Locale.getDefault())
                                    .contains(Constants.NO)
                            }.forEach {
                                filteredList.add(it)
                            }
                            Log.e("filteredList",filteredList.size.toString())
                            if (filteredList.size != 0) {
                                binding.notificationImgView.setImageResource(R.drawable.ic_notifications)
                            } else {
                                binding.notificationImgView.setImageResource(R.drawable.ic_notification)
                            }
                        }
                    } else {
                        binding.notificationImgView.setImageResource(R.drawable.ic_notification)
                    }
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
                            Utils.showProgress(this@MainActivity)
                        }
                    }
                    LoadingState.Status.SUCCESS -> {
                        Utils.hideProgress()
                    }
                    LoadingState.Status.FAILED -> {
                        Utils.hideProgress()
                        val errorData = loadingState.errorData
                        val errorCode = errorData!!.errorCode

                        checkErrorCode(errorData.errorCode)

                    }
                }
            }
        }
    }
}