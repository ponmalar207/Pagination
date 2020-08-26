package com.example.kotlinpagination

data class RequestData(
    var pageSize: Int,
    var pageNumber: Int,
    var language: Int
)
