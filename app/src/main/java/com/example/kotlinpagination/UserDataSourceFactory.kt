package com.example.kotlinpagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class UserDataSourceFactory(private val requestData: RequestData) : DataSource.Factory<Int, User>() {
    val userLiveDataSource = MutableLiveData<UserDataSource>()
    override fun create(): DataSource<Int, User> {
        val userDataSource = UserDataSource(requestData)
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }
}