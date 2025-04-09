package com.brunodegan.pokedex.base.network.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
open class BaseApiData : Parcelable

@Serializable
data class NetworkResponse<T>(
    val data: T,
)

typealias ApiData = BaseApiData