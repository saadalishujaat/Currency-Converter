package com.example.currencyconverterswipebox.models

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class ExchangeRates (
    val result: String,
    val documentation: String,

    @SerialName("terms_of_use")
    val termsOfUse: String,

    @SerialName("time_last_update_unix")
    val timeLastUpdateUnix: Long,

    @SerialName("time_last_update_utc")
    val timeLastUpdateUTC: String,

    @SerialName("time_next_update_unix")
    val timeNextUpdateUnix: Long,

    @SerialName("time_next_update_utc")
    val timeNextUpdateUTC: String,

    @SerialName("base_code")
    val baseCode: String,

    @SerialName("conversion_rates")
    val conversionRates: Map<String, Double>
)