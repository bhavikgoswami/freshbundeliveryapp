package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.SplashActivityBinding
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import io.paperdb.Paper

class SplashActivity : BaseActivity() {
    private lateinit var binding: SplashActivityBinding
    private val splashScreenDelay: Long = 3000
    lateinit var activityIntent: Intent
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        Paper.book().write(Constants.SCREEN_WIDTH, getWidth())
        init()
    }

    private fun init() {
        YoYo.with(Techniques.BounceInUp)
            .duration(2000)
            .repeat(0)
            .playOn(binding.logoImgView)


     Handler(Looper.getMainLooper()).postDelayed({


                if (Paper.book().contains(Constants.USER_TOKEN)) {
                if (Paper.book().contains(Constants.USER_DETAILS)) {
                    val activityIntent = Intent(
                            this@SplashActivity,
                            VerifyMpinActivity::class.java
                        )
                        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(activityIntent)
                        overridePendingTransition(0, 0)
                        finish()
                    }

            }else {
                    activityIntent = Intent(this@SplashActivity, MobileNumberActivity::class.java)
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(activityIntent)
                    overridePendingTransition(0, 0)
                    finish()
                }

        }, splashScreenDelay)
    }
}