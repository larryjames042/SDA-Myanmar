package com.church.sdahymnal.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.church.sdahymnal.R
import com.church.sdahymnal.database.Song
import java.lang.NumberFormatException


class SearchAdapter(val listener: OnItemClickListener) : ListAdapter<Song, SearchAdapter.ViewHolder>(SearchListDiffCallback()), Filterable {

    private var list1 = mutableListOf<Song>()
    private var listAll = mutableListOf<Song>()
    var selected_position = -1

    val mFilter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            selected_position = -1
            var filteredList = mutableListOf<Song>()
            if(constraint.isNullOrEmpty()){
                filteredList.addAll(listAll)
            }else{
                listAll.forEach {
                    try{
                        val num = constraint.toString().toDouble()
                        if(it.songNumber.toString().contains(constraint!!.toString())){
                            filteredList.add(it)
                        }
                    }catch (e : NumberFormatException){
                        if(it.songTitle.toUpperCase().contains(constraint!!.toString().toUpperCase())){
                            filteredList.add(it)
                        }
                    }

                }
            }

            val filterResult = FilterResults()
            filterResult.values = filteredList
            return filterResult
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            list1.clear()
            list1.addAll(results?.values as Collection<Song>)
            submitList(list1)
            notifyDataSetChanged()
        }
    }


    override fun getFilter(): Filter {
        return mFilter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val v =  layoutInflater.inflate(R.layout.item_content, parent, false)
        return ViewHolder(v, parent)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val data = getItem(position)
//        if(selected_position==position){
//            holder.container.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.colorPurple100))
//            holder.txtCategory.background = holder.container.context.getDrawable(R.drawable.bg_category_selected_item)
//        }else{
//            holder.txtCategory.background = null
//            holder.container.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.white))
//        }

        holder.bind(data, listener)
    }

    override fun onCurrentListChanged(previousList: MutableList<Song>, currentList: MutableList<Song>) {
        super.onCurrentListChanged(previousList, currentList)
        if(previousList.isEmpty()){
            listAll.addAll(currentList)
            list1.addAll(currentList)
        }
    }

    inner class ViewHolder(itemView : View, parent: ViewGroup): RecyclerView.ViewHolder(itemView){
        val songNumber = itemView.findViewById<TextView>(R.id.txt_song_number)
        val songTitle = itemView.findViewById<TextView>(R.id.txt_song_name)
        val layoutContainer = itemView.findViewById<ViewGroup>(R.id.layout_container)

        fun bind(data : Song, listener: OnItemClickListener){
            songNumber.text = data.songNumber.toString()
            songTitle.text = data.songTitle
            layoutContainer.setOnClickListener {
                listener.onItemClick(data)
            }

        }

    }

}

class SearchListDiffCallback : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}

interface OnItemClickListener{
    fun onItemClick(song : Song)
}