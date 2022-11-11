package com.greypixstudio.broovisdeliveryapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.greypixstudio.broovisdeliveryapp.di.*
import io.paperdb.Paper
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    companion object{
        var appContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        registerActivityLifecycleCallbacks(AppLifecycleTracker())
        Paper.init(this@App)

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    userViewModel,
                    userRepository,
                    userAPIInterface,
                    retrofitModule,
                    uploadDocumentAPIInterface,
                    uploadDocumentRepository,
                    uploadDocumentViewModel,
                    orderDetailApiInterface,
                    orderDetailRepository,
                    orderDetailViewModel,
                    notificationInterface,
                    notificationRepository,
                    notificationViewModel
                )
            )
        }
    }
}

class AppLifecycleTracker : Application.ActivityLifecycleCallbacks {

    private var numStarted = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        if (numStarted == 0) {
            // app went to foreground
        }
        numStarted++
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        numStarted--
        if (numStarted == 0) {
            // app went to background
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

}