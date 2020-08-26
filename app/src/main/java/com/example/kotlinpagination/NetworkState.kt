package com.example.kotlinpagination

class NetworkState(var status: Status, var msg: String) {

    enum class Status {
        RUNNING, SUCCESS, FAILED
    }

    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")
    }
}
