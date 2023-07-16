package com.thiago.marvelcompose.model.api


import com.thiago.marvelcompose.model.CharactersApiResponse
import retrofit2.Call
import retrofit2.http.*

interface MarvelApi {

    @GET("characters")
    fun getCharacters(@Query("nameStartsWith") name : String) : Call<CharactersApiResponse>
}