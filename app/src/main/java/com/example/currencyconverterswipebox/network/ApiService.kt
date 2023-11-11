package com.example.currencyconverterswipebox.network

import com.example.currencyconverterswipebox.config.ConstURL
import com.example.currencyconverterswipebox.models.ExchangeRates
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ApiService @Inject constructor() {



    private val client: HttpClient = HttpClient(OkHttp) {

        install(DefaultRequest) {
            headers.append("Content-Type", "application/json")
        }

        install(HttpCache)

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }


        install(HttpRequestRetry) {
            maxRetries = 3
            retryIf { request, response ->
                !response.status.isSuccess()
            }
            delayMillis { retry ->
                retry * 5000L
            }
        }

        engine {

        }
/*        engine {
            requestTimeout = 100_000
        }*/
    }


    suspend fun getRates(currencyCode: String): ExchangeRates {
        return client.get {
            url(ConstURL.getRates(currencyCode))
        }.body()
    }


}


