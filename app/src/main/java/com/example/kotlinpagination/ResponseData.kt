package com.example.kotlinpagination

data class ResponseData(
    var status: Int,
    var message: String,
    var responseData: UserCount?
)