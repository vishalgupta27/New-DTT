package com.appdtt.dtt

import android.telecom.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query


private const val BASE_URLs = "https://www.partwk.com/AudioApp/public/"

interface Methodsinterface {
    @POST("api/user/login")
    fun loginData(
        @Query("email") email:String,
        @Query("password") password: String ): retrofit2.Call<Map<Any,Any>?>?

    /* @POST("api/user/login")
     fun login(@Body request: LoginRequest): Call<Map<Any,Any>?>
 */
}

object MethodLogin {
    val methodsinstance: Methodsinterface

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URLs)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        methodsinstance = retrofit.create(Methodsinterface::class.java)
    }
}