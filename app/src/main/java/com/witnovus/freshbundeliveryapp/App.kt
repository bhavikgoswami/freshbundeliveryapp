package com.witnovus.freshbundeliveryapp

import android.app.Application
import android.content.Context
import com.witnovus.freshbundeliveryapp.di.*
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