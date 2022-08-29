package com.witnovus.freshbundeliveryapp.di

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.witnovus.freshbundeliveryapp.viewmodel.notification.NotificationRepository
import com.witnovus.freshbundeliveryapp.viewmodel.notification.NotificationViewModel
import com.witnovus.freshbundeliveryapp.network.NotificationInterface
import com.witnovus.freshbundeliveryapp.network.OrderDetailApiInterface
import com.witnovus.freshbundeliveryapp.network.UploadDocumentAPIInterface
import com.witnovus.freshbundeliveryapp.network.UserAPIInterface
import com.witnovus.freshbundeliveryapp.utils.Constants
import com.witnovus.freshbundeliveryapp.viewmodel.orderdetail.OrderDetailRepository
import com.witnovus.freshbundeliveryapp.viewmodel.orderdetail.OrderDetailViewModel
import com.witnovus.freshbundeliveryapp.viewmodel.uploaddocument.UploadDocumentRepository
import com.witnovus.freshbundeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import com.witnovus.freshbundeliveryapp.viewmodel.user.UserRepository
import com.witnovus.freshbundeliveryapp.viewmodel.user.UserViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val userViewModel = module {
    viewModel {
        UserViewModel(get())
    }
}

val userRepository = module {
    single {
        UserRepository(get(), get())
    }
}

val userAPIInterface = module {
    fun provideApi(retrofit: Retrofit): UserAPIInterface {
        return retrofit.create(UserAPIInterface::class.java)
    }
    single { provideApi(get()) }
}

val uploadDocumentAPIInterface = module {
    fun provideApi(retrofit: Retrofit): UploadDocumentAPIInterface {
        return retrofit.create(UploadDocumentAPIInterface::class.java)
    }
    single { provideApi(get()) }
}

val uploadDocumentRepository = module {
    single {
       UploadDocumentRepository(get(), get())
    }
}

val uploadDocumentViewModel = module {
    viewModel {
        UploadDocumentViewModel(get())
    }
}

val orderDetailApiInterface = module {
    fun provideApi(retrofit: Retrofit): OrderDetailApiInterface {
        return retrofit.create(OrderDetailApiInterface::class.java)
    }
    single { provideApi(get()) }
}

val orderDetailRepository = module {
    single {
        OrderDetailRepository(get(),get())
    }
}

val orderDetailViewModel = module {
    viewModel {
        OrderDetailViewModel(get())
    }
}

val notificationViewModel = module {
    viewModel {
        NotificationViewModel(get())
    }
}

val notificationRepository = module {
    single {
        NotificationRepository(get(), get())
    }
}

val notificationInterface = module {
    fun provideApi(retrofit: Retrofit): NotificationInterface {
        return retrofit.create(NotificationInterface::class.java)
    }
    single { provideApi(get()) }
}
val retrofitModule = module {

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        okHttpClientBuilder.connectTimeout(240, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(240, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(240, TimeUnit.SECONDS)
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClientBuilder.addInterceptor(logging)
        return okHttpClientBuilder.build()
    }

    fun provideHttpClientWithPin(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        okHttpClientBuilder.connectTimeout(120, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(120, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(120, TimeUnit.SECONDS)
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClientBuilder.addInterceptor(logging)
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        val logging = okhttp3.logging.HttpLoggingInterceptor()
        // set your desired log level
        logging.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY)
        val httpClient = okhttp3.OkHttpClient.Builder()
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging)
        //httpClient.retryOnConnectionFailure(true);

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()

        val builder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(httpClient.build())
            .client(client)

        val httpClientToken = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val ongoing = chain.request().newBuilder()
                val request = ongoing.build()
                Log.e("Request",request.toString())
                chain.proceed(request)
            }.build()

        builder.client(httpClientToken)
        return builder.build()
    }

    single { provideGson() }
    single { provideHttpClientWithPin() }
    single { provideRetrofit(get(), get()) }
}