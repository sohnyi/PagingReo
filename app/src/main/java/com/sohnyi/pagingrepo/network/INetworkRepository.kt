package com.sohnyi.pagingrepo.network

interface INetworkRepository {
    fun <T> obtainRetrofitService(service: Class<T>): T
}