package br.com.natanaelribeiro.eventsdata.models

import com.google.gson.annotations.SerializedName

data class Coupon(
    @SerializedName("id") val id: String,
    @SerializedName("eventId") val eventId: String,
    @SerializedName("discount") val discount: Int
)