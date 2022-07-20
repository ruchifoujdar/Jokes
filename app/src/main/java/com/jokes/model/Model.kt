package com.jokes.model

import com.google.gson.annotations.SerializedName

data class Jokes (
    @SerializedName("id") val id: String?,
    @SerializedName("icon_url") val icon_url: String?,
    @SerializedName("value") val joke: String?
)

data class Categories(val category: String?)

//data class Results(val result : List<Jokes>  = arrayListOf())

data class Results(val result : ArrayList<Jokes> = arrayListOf())
