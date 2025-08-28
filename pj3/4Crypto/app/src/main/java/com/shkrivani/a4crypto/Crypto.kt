package com.shkrivani.a4crypto

data class Crypto(
    val id: String,
    val rank: String, // Add rank
    val name: String,
    val symbol: String,
    val priceUsd: String,
    val supply: String,
    val changePercent24Hr: String,
    val explorer: String
)
