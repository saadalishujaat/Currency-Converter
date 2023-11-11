package com.example.currencyconverterswipebox.config


object ConstURL {

    private const val baseUrl = "https://v6.exchangerate-api.com/v6"
    private const val apiKey = "a4d26b7493e0fa0c79d8a3cc"

    fun getRates(currencyCode: String): String {
        return "$baseUrl/$apiKey/latest/$currencyCode"
    }
}