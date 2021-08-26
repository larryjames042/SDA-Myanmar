package com.church.sdahymnal.ui.tableofcontent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.church.sdahymnal.R
import com.church.sdahymnal.database.Song
import com.church.sdahymnal.utils.RecyclerViewItemClickListener

class SongListAdapter( val listener : RecyclerViewItemClickListener) :
    ListAdapter<Song, SongListAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Song>(){
            override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
               return  oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongListAdapter.ViewHolder, position: Int) {
        val song = getItem(position)
        holder.songTitle.text = song?.songTitle
        holder.songNumber.text = song?.songNumber.toString()
        holder.layoutContainer.setOnClickListener {
            if (song != null) {
                listener.onItemClick(song)
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val songNumber = itemView.findViewById<TextView>(R.id.txt_song_number)
        val songTitle = itemView.findViewById<TextView>(R.id.txt_song_name)
        val layoutContainer = itemView.findViewById<ViewGroup>(R.id.layout_container)
    }
}

//class SongListAdapter(val list : List<Song>, val listener : RecyclerViewItemClickListener) : RecyclerView.Adapter<SongListAdapter.ViewHolder>(){
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListAdapter.ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: SongListAdapter.ViewHolder, position: Int) {
//        val song = list[position]
//        holder.songTitle.text = song.songTitle
//        holder.songNumber.text = song.songNumber.toString()
//        holder.layoutContainer.setOnClickListener {
//            listener.onItemClick(song)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
//        val songNumber = itemView.findViewById<TextView>(R.id.txt_song_number)
//        val songTitle = itemView.findViewById<TextView>(R.id.txt_song_name)
//        val layoutContainer = itemView.findViewById<ViewGroup>(R.id.layout_container)
//    }
//}