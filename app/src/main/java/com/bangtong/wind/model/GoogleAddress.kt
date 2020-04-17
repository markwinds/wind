package com.bangtong.wind.model

data class GoogleAddress(
    val results: List<Result>,
    val status: String
)

data class Result(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String,
    val types: List<String>
)

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)

data class Geometry(
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
)

data class Northeast(
    val lat: Double,
    val lng: Double
)

data class Southwest(
    val lat: Double,
    val lng: Double
)