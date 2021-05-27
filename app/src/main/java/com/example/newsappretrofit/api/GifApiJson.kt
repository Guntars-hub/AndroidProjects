package com.example.newsappretrofit.api

data class GifApiJson(
    val `data`: List<Data>,
    val meta: Meta,
    val pagination: Pagination
)