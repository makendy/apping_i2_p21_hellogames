package com.example.hellogames

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_game_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)

        // retrieve the intent that caused the activity to open
        val originIntent = intent
        // extract data from the intent
        val id = originIntent.getStringExtra("GAME_ID")!!

        val baseURL = "https://androidlessonsapi.herokuapp.com/api/"
        // Use GSON library to create our JSON parser
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        // Create a Retrofit client object targeting the provided URL
        // and add a JSON converter (because we are expecting json responses)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()

        val service = retrofit.create(WSInterface::class.java)

        val callback = object : Callback<GameDetails> {
            override fun onFailure(call: Call<GameDetails>, t: Throwable) {
                Toast.makeText(this@GameDetailsActivity,
                    "WS failed : Unable to find game details", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<GameDetails>, response: Response<GameDetails>) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        val data : GameDetails = response.body()!!
                        Glide
                            .with(this@GameDetailsActivity)
                            .load(data.picture)
                            .centerCrop()
                            .into(activity_game_details_img)

                        activity_game_details_name.text =  ""+ activity_game_details_name.text + data.name
                        activity_game_details_type.text = ""+ activity_game_details_type.text + data.type
                        activity_game_details_players.text = ""+ activity_game_details_players.text + data.players
                        activity_game_details_year.text = ""+ activity_game_details_year.text + data.year.toString()
                        activity_game_details_desc.text = ""+ data.description_en

                        activity_game_details_button.setOnClickListener {
                            val url = data.url
                            // Define an implicit intent
                            val implicitIntent = Intent(Intent.ACTION_VIEW)
                            // Add the required data in the intent (here the URL we want to open)
                            implicitIntent.data = Uri.parse(url)
                            // Launch the intent
                            startActivity(implicitIntent)

                        }

                    }
                }

            }

        }
        service.getGameDetails(id.toInt()).enqueue(callback)
    }
}
