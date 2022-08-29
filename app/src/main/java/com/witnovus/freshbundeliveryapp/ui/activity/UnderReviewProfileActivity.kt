package com.witnovus.freshbundeliveryapp.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.UnderReviewProfileActivityBinding
import com.witnovus.freshbundeliveryapp.ui.base.BaseActivity

class UnderReviewProfileActivity : BaseActivity() {

    lateinit var binding: UnderReviewProfileActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_under_review_profile)
        init()
    }

    fun init() {
    }
}