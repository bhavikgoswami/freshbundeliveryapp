package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.HelpAndSupportActivityBinding
import com.greypixstudio.broovisdeliveryapp.databinding.ToolbarLayoutBinding
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Constants.Companion.SUPPORT_EMAIL

class HelpAndSupportActivity : BaseActivity() {

    private lateinit var binding: HelpAndSupportActivityBinding
    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help_and_support)
        init()
        binding.emailBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$SUPPORT_EMAIL") // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.lbl_support_broovis))
            startActivity(Intent.createChooser(intent, "Chooser Title"))
        }

        binding.callBtn.setOnClickListener {
            checkPermission()
        }
        binding.faqBtn.setOnClickListener {
            val activityIntent = Intent(this@HelpAndSupportActivity, WebViewActivity::class.java)
            activityIntent.putExtra(Constants.WEB_VIEW_URL, Constants.FAQ_URL)
            activityIntent.putExtra(Constants.SCREEN_TITLE, getString(R.string.lbl_faqs))
            startActivity(activityIntent)
            overridePendingTransition(0, 0)
        }
    }
    private fun init() {
        toolbarLayoutBinding = binding.helpAndSupportToolbar
        binding.helpAndSupportToolbar.toolbarNametxtView.text = getText(R.string.lbl_help_amp_support)
        binding.helpAndSupportToolbar.backACImageView.setOnClickListener {
            onBackPressed()
        }
    }
    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CALL_PHONE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42
                )
            }
        } else {
            // Permission has already been granted
            callPhone()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                callPhone()
            } else {

            }
            return
        }
    }

    fun callPhone() {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constants.PHONE_NUMBER))
        startActivity(intent)
    }

}