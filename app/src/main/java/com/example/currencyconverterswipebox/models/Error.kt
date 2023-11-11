package com.example.currencyconverterswipebox.models

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class Error (
    val result: String,
    val documentation: String,

    @SerialName("terms-of-use")
    val termsOfUse: String,

    @SerialName("error-type")
    val errorType: String
)
