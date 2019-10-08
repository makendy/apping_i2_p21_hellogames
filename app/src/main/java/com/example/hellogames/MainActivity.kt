package com.example.hellogames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        val callback = object : Callback<List<GameList>> {
            override fun onFailure(call: Call<List<GameList>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "WS failed", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<GameList>>,
                response: Response<List<GameList>>
            ) {
                if (response.code() == 200){
                    if(response.body() != null){
                        val data : List<GameList> = response.body()!! // "!!" pour garantir que c'est non null
                        // display performance optimization when list widget size does not change
                        RecyclerListGame.setHasFixedSize(true)
                        // here we specify this is a standard vertical list
                        RecyclerListGame.layoutManager = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.VERTICAL,
                            false)
                        //decoration
                        RecyclerListGame.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
                        // attach an adapter and provide some data

                        val myGameClickListener = View.OnClickListener {
                            // we retrieve the row position from its tag
                            val position = it.tag as Int
                            val clickedItem = data[position]
                            // do stuff
                            Toast.makeText(
                                this@MainActivity,
                                "Clicked " + clickedItem.name,
                                Toast.LENGTH_SHORT)
                                .show()

                            // Create an explicit intent
                            val explicitIntent = Intent(this@MainActivity,
                                                            GameDetailsActivity::class.java)
                            // Insert extra data in the intent
                            explicitIntent.putExtra("GAME_ID", clickedItem.id.toString())
                            // Start the other activity by sending the intent
                            startActivity(explicitIntent)
                        }
                        // attach an adapter and provide some data
                        val recyclerAdapter = GameListAdapter(data,this@MainActivity, myGameClickListener)
                        RecyclerListGame.adapter = recyclerAdapter
                    }
                }
            }

        }
        service.listGame().enqueue(callback)
    }
}
