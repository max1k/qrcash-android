package ru.mxk.qrcash

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mxk.qrcash.repository.QRCashRepository
import ru.mxk.qrcash.service.QRCashService


class App : Application() {
    private val qrCashRepository: QRCashRepository = createQrCashRepository()
    val qrCashService = QRCashService(qrCashRepository)

    private fun createQrCashRepository(): QRCashRepository {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.224.9.136:8099/qrcash/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        return retrofit.create(QRCashRepository::class.java)
    }
}