package com.aisyahpn0033.qrcodegenerator.ui.theme.network

import com.aisyahpn0033.qrcodegenerator.ui.theme.data.RemoteQr
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface QrApiService {
    @GET("users_qrcodes.json")
    suspend fun getUserQrs(): List<RemoteQr>
}

object RetrofitClient {
    private const val BASE_URL = "https://aisyahpn.github.io/users_qrcodes_api/"

    val api: QrApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QrApiService::class.java)
    }
}