package com.yefimoyevhen.redditclient.model.response

data class Data(
    val after: String,
    val before: String,
    val children: List<Children>,
    val dist: Int,
    val geo_filter: String,
    val modhash: String
)