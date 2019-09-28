package br.com.natanaelribeiro.eventsdata.models

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("people") val people: List<Person>,
    @SerializedName("date") val date: Long,
    @SerializedName("descripion") val description: String,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("longitude") val longitude: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("price") val price: Double,
    @SerializedName("title") val title: String,
    @SerializedName("id") val id: String,
    @SerializedName("cupons") val cupons: List<Coupon>
)