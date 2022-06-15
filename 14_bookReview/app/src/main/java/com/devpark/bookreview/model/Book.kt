package com.devpark.bookreview.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Parcelize은 코틀린 라이브러리 모델 데이터 직렬화
 */
@Parcelize
data class Book(
    @SerializedName("itemId") val id: Long?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val priceSales: String?,
    @SerializedName("coverSmallUrl") val coverSmallUrl: String?,
    @SerializedName("link") val mobileLink: String?
) : Parcelable
