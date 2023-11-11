package com.example.currencyconverterswipebox.repository

import com.example.currencyconverterswipebox.models.ExchangeRates
import com.example.currencyconverterswipebox.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatesRepository
@Inject
constructor(private val apiService: ApiService) {

    fun getExchangeRates(currencyCode: String): Flow<ExchangeRates> = flow {
        emit(apiService.getRates(currencyCode))
    }.flowOn(Dispatchers.IO)

}



