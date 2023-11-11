package com.example.currencyconverterswipebox.network

import com.example.currencyconverterswipebox.models.ExchangeRates


sealed class ApiState {
    object Loading : ApiState()
    object Empty : ApiState()
    class Success(val exchangeRates: ExchangeRates) : ApiState()
    class Failure(val error: Error) : ApiState()
}
