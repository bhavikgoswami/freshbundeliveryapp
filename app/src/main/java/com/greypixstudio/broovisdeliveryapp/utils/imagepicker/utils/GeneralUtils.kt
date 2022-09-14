package com.greypixstudio.broovisdeliveryapp.utils.imagepicker.utils

import android.app.Activity
import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.view.View.*
import android.view.WindowInsets
import android.view.WindowManager
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.PickerActivityBinding
import java.text.SimpleDateFormat
import java.util.*


object GeneralUtils {

    fun manipulateBottomSheetVisibility(activity: Activity, slideOffSet: Float, mBinding: PickerActivityBinding, count: Int){

        mBinding.recyclerViewInstantMedia.alpha = 1 - slideOffSet
        mBinding.constraintCheck.alpha = 1 - slideOffSet
        mBinding.constraintBottomSheetTop.alpha =     slideOffSet
        mBinding.recyclerViewBottomSheetMedia.alpha = slideOffSet

        if ((1 - slideOffSet) == 0f && mBinding.recyclerViewInstantMedia.visibility == VISIBLE){
            mBinding.recyclerViewInstantMedia.visibility = GONE
            mBinding.constraintCheck.visibility = GONE
        }else if(mBinding.recyclerViewInstantMedia.visibility == GONE && (1 - slideOffSet) > 0f){
            mBinding.recyclerViewInstantMedia.visibility = VISIBLE
            if (count > 0) mBinding.constraintCheck.visibility = VISIBLE
        }

        if (slideOffSet > 0f && mBinding.recyclerViewBottomSheetMedia.visibility == INVISIBLE){
            mBinding.recyclerViewBottomSheetMedia.visibility = VISIBLE
            mBinding.constraintBottomSheetTop.visibility = VISIBLE
            showStatusBar(activity)
        }else if (slideOffSet == 0f && mBinding.recyclerViewBottomSheetMedia.visibility == VISIBLE){
            mBinding.recyclerViewBottomSheetMedia.visibility = INVISIBLE
            mBinding.constraintBottomSheetTop.visibility = GONE
            hideStatusBar(activity)
        }
    }

    fun hideStatusBar(appCompatActivity: Activity) {
        synchronized(appCompatActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                appCompatActivity.window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                appCompatActivity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }
    }

    fun showStatusBar(appCompatActivity: Activity) {
        synchronized(appCompatActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                appCompatActivity.window.insetsController?.show(WindowInsets.Type.statusBars())
            } else {
                appCompatActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }
    }

    fun getStringDate(context: Context, time: Long): String{
        //
        val date = Date(time * 1000)
        val calendar = Calendar.getInstance()
        calendar.time = date

        val lastMonth = Calendar.getInstance()
        val lastWeek = Calendar.getInstance()
        val recent = Calendar.getInstance()
        lastMonth.add(Calendar.DAY_OF_MONTH, -Calendar.DAY_OF_MONTH)
        lastWeek.add(Calendar.DAY_OF_MONTH, -7)
        recent.add(Calendar.DAY_OF_MONTH, -2)
        return if (calendar.before(lastMonth)) {
            SimpleDateFormat("MMMM", Locale.ENGLISH).format(date)
        } else if (calendar.after(lastMonth) && calendar.before(lastWeek)) {
            context.resources.getString(R.string.last_month)
        } else if (calendar.after(lastWeek) && calendar.before(recent)) {
            context.resources.getString(R.string.last_week)
        } else {
            context.resources.getString(R.string.recent)
        }
    }


    fun getScreenWidth(activity: Activity): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            return windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}