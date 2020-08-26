package com.example.kotlinpagination

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
interface ApiService {
    @POST("api/getAstro")
    fun getUsers(@Body requestData: RequestData): Call<ResponseData>
}