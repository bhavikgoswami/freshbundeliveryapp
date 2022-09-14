package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.UnderReviewProfileActivityBinding
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity

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