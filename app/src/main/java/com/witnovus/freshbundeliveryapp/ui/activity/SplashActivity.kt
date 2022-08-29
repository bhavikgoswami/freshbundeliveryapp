package com.witnovus.freshbundeliveryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.SplashActivityBinding
import com.witnovus.freshbundeliveryapp.ui.base.BaseActivity
import com.witnovus.freshbundeliveryapp.utils.Constants
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

        YoYo.with(Techniques.Wave)
            .duration(1500)
            .repeat(0)
            .playOn(binding.textLogoImgView)

        YoYo.with(Techniques.SlideInUp)
            .duration(1000)
            .repeat(0)
            .playOn(binding.textLogoImgView)

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