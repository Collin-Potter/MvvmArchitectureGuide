package com.example.mvvmarchitectureguide.api

import com.google.gson.annotations.SerializedName

data class ResultsResponse<T> (
    @SerializedName("count")
    val count: Int,
    @SerializedName("results")
    val results: List<T>
)