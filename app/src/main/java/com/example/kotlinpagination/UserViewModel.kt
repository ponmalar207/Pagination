package com.example.kotlinpagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList


class UserViewModel : ViewModel() {
    private var networkState: LiveData<NetworkState>? = null
    private lateinit var userPagedList: LiveData<PagedList<User>>
    fun getUsersList(requestData: RequestData): LiveData<PagedList<User>> {
        val itemDataSourceFactory = UserDataSourceFactory(requestData)
        val config = PagedList.Config.Builder().setEnablePlaceholders(false)
            .setPageSize(10).build()
        networkState =
            Transformations.switchMap(itemDataSourceFactory.userLiveDataSource) { dataSource -> dataSource.getNetworkState() }
        userPagedList = LivePagedListBuilder(itemDataSourceFactory, config).build()
        return userPagedList
    }

    fun getNetworkState(): LiveData<NetworkState>? {
        return networkState
    }
}