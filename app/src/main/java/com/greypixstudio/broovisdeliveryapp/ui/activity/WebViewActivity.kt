package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.WebViewActivityBinding
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import kotlinx.android.synthetic.main.toolbar_layout.*

class WebViewActivity : BaseActivity() {

    private lateinit var binding: WebViewActivityBinding
    private var url = ""
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
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
        backACImageView.setOnClickListener { finish() }
        toolbarNametxtView.text = title

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