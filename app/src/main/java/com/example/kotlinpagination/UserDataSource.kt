package com.example.kotlinpagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Response

class UserDataSource(val requestData: RequestData) : PageKeyedDataSource<Int, User>() {
    private val networkState: MutableLiveData<NetworkState> = MutableLiveData<NetworkState>()
    private val initialLoading: MutableLiveData<NetworkState> = MutableLiveData<NetworkState>()
    fun getNetworkState(): MutableLiveData<NetworkState>? {
        return networkState
    }

    fun getInitialLoading(): MutableLiveData<NetworkState>? {
        return initialLoading
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        initialLoading.postValue(NetworkState.LOADING)
        networkState.postValue(NetworkState.LOADING)

        RetrofitClient.apiService.getUsers(requestData)
            .enqueue(object : retrofit2.Callback<ResponseData> {
                override fun onResponse(
                    call: Call<ResponseData>,
                    response: Response<ResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!
                        val responseItems = apiResponse.responseData?.userList
                        responseItems.let {
                            callback.onResult(
                                responseItems!!,
                                null,
                                (requestData.pageNumber) + 1
                            )
                            initialLoading.postValue(NetworkState.LOADED)
                            networkState.postValue(NetworkState.LOADED)
                        }
                    } else {
                        initialLoading.postValue(
                            NetworkState(
                                NetworkState.Status.FAILED,
                                response.message()
                            )
                        )
                        networkState.postValue(
                            NetworkState(
                                NetworkState.Status.FAILED,
                                response.message()
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    t.printStackTrace()
                    val errorMessage = t.message ?: "unknown error"
                    networkState.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
                }
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        networkState.postValue(NetworkState.LOADING)
        RetrofitClient.apiService.getUsers(requestData.apply { pageNumber = params.key })
            .enqueue(object : retrofit2.Callback<ResponseData> {
                override fun onResponse(
                    call: Call<ResponseData>,
                    response: Response<ResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!.responseData
                        val responseItems = apiResponse?.userList
                        val nextPageKey =
                            if (params.requestedLoadSize > responseItems?.size ?: 0) null else params.key + 1
                        if (responseItems != null) {
                            callback.onResult(responseItems, nextPageKey)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(
                                NetworkState(
                                    NetworkState.Status.FAILED,
                                    "No more data!"
                                )
                            )
                        }
                    } else networkState.postValue(
                        NetworkState(
                            NetworkState.Status.FAILED,
                            response.message()
                        )
                    )
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    t.printStackTrace()
                    val errorMessage =
                        t.message ?: "unknown error"
                    networkState.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
    }
}
