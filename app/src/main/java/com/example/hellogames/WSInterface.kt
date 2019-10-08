package com.example.hellogames

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WSInterface {
    @GET("game/list")
    fun listGame() : Call<List<GameList>>

    @GET("game/details")
    fun getGameDetails(@Query("game_id") id: Int): Call<GameDetails>
}