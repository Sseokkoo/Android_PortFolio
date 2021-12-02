package com.ko.ksj.portfolio.repository.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NaverClient{

    companion object {

        private var Naver_INTERFACE: NaverAPI? = null

        val BASE_URL_NAVER_API = "https://openapi.naver.com/"

        val version = "v1"

        private var retrofit: Retrofit? = null

        private fun initNaverClient(): Retrofit {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

            val mUrl = "$BASE_URL_NAVER_API$version/"

            retrofit = Retrofit.Builder()
                .baseUrl(mUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit as Retrofit
        }

        private fun getNaverClient(): Retrofit? {
            if (retrofit == null) {
                initNaverClient()
            }
            return retrofit
        }

        private fun <S> createService(serviceClass: Class<S>?) {
            Naver_INTERFACE = getNaverClient()?.create(serviceClass) as NaverAPI
        }

        fun getService(): NaverAPI? {
            if (Naver_INTERFACE == null) {
                createService(NaverAPI::class.java)
            }
            return Naver_INTERFACE
        }
    }
}