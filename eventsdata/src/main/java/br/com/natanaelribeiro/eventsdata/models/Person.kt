package br.com.natanaelribeiro.eventsdata.models

import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("id") val id: String,
    @SerializedName("eventId") val eventId: String,
    @SerializedName("name") val name: String,
    @SerializedName("picture") val picture: String
)