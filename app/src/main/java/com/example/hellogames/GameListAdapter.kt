package com.example.hellogames

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GameListAdapter (val data : List<GameList>,
                       val context : Context,
                       private val onItemClickListener: View.OnClickListener) :
    RecyclerView.Adapter<GameListAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // retrieve the item at the specified position
        val currentItem = data[position]
        // put the data
        holder!!.gameTextview.text = currentItem.name
        Glide
            .with(holder.itemView)
            .load(currentItem.picture)
            .centerCrop()
            .into(holder.gameImageView)
        holder.itemView.tag = position
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val gameTextview : TextView = itemView.findViewById(R.id.main_activity_game_name)
        val gameImageView : ImageView = itemView.findViewById(R.id.main_activity_image_game)
    }

    // called when a new viewholder is required to display a row
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ViewHolder {
        // create the row from a layout inflater
        val rowView = LayoutInflater
            .from(context)
            .inflate(R.layout.list_items_game_list, parent, false)

        // attach the onclicklistener
        rowView.setOnClickListener(onItemClickListener)
        // create a ViewHolder using this rowview
        val viewHolder = ViewHolder(rowView)
        // return this ViewHolder. The system will make sure view holders
        // are used and recycled
        return viewHolder
    }


    override fun getItemCount(): Int {
        return data.size
    }
}