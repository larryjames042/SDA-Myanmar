package com.church.sdahymnal.ui.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.church.sdahymnal.R
import com.church.sdahymnal.database.SongWithBookInfo
import com.church.sdahymnal.utils.RecyclerViewItemClickListener

class FavouriteAdapter( val listener : RecyclerViewItemClickListener) :
        ListAdapter<SongWithBookInfo, FavouriteAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SongWithBookInfo>(){
            override fun areItemsTheSame(oldItem: SongWithBookInfo, newItem: SongWithBookInfo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SongWithBookInfo, newItem: SongWithBookInfo): Boolean {
                return  oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteAdapter.ViewHolder, position: Int) {
        val song = getItem(position)
        holder.songTitle.text = song?.songTitle
        holder.songNumber.text = song?.songNumber.toString()
        holder.songIndex.text = (position+1).toString()
        holder.bookName.text = song.name
        holder.layoutContainer.setOnClickListener {
            if (song != null) {
                listener.onItemClick(song)
            }
        }

        holder.imgDelete.setOnClickListener {
                listener.onDeleteClick(song)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val songNumber = itemView.findViewById<TextView>(R.id.txt_song_number)
        val songTitle = itemView.findViewById<TextView>(R.id.txt_song_name)
        val songIndex = itemView.findViewById<TextView>(R.id.txt_index)
        val imgDelete = itemView.findViewById<ImageView>(R.id.img_remove)
        val bookName = itemView.findViewById<TextView>(R.id.txt_book_name)
        val layoutContainer = itemView.findViewById<ViewGroup>(R.id.layout_container)
    }
}