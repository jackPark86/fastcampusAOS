package com.devpark.bookreview.model

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("itemId") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val priceSales: String,
    @SerializedName("coverSmallUrl") val coverSmallUrl: String,
    @SerializedName("link") val mobileLink: String
    )
