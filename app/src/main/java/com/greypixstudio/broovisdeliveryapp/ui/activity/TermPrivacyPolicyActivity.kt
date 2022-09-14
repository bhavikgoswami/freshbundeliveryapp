package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.TermPrivacyPolicyActivityBinding
import com.greypixstudio.broovisdeliveryapp.databinding.ToolbarLayoutBinding
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants

class TermPrivacyPolicyActivity : BaseActivity() {

    private lateinit var binding: TermPrivacyPolicyActivityBinding
    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding

    private var url = ""
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_term_privacy_policy)

        if(intent.hasExtra(Constants.WEB_VIEW_URL)){
            url = intent.getStringExtra(Constants.WEB_VIEW_URL).toString()
            title = intent.getStringExtra(Constants.SCREEN_TITLE).toString()
            if(url.trim().isEmpty())
                finish()
        }else{
            finish()
        }
        init()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun init(){

        toolbarLayoutBinding = binding.termPolicyToolbar
        binding.termPolicyToolbar.toolbarNametxtView.text = getText(R.string.lbl_term_privacy_amp_policy)
        binding.termPolicyToolbar.backACImageView.setOnClickListener {
            onBackPressed()
        }
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.progressBar.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding.progressBar.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }
        }
        binding.webView.loadUrl(url)
    }
}