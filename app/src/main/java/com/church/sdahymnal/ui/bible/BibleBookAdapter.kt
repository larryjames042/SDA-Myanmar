package com.church.sdahymnal.ui.bible

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.church.sdahymnal.R
import com.church.sdahymnal.data.BibleBook
import com.church.sdahymnal.utils.RecyclerViewItemClickListener

class BibleBookAdapter(val listener : RecyclerViewItemClickListener) : ListAdapter<BibleBook, BibleBookAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BibleBook>(){
            override fun areItemsTheSame(oldItem: BibleBook, newItem: BibleBook): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BibleBook, newItem: BibleBook): Boolean {
                return  oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.item_bible_book, parent, false )


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bible = getItem(position)
        holder.bibleBookName.text = bible.bookNameMm
        holder.container.setOnClickListener { listener.onItemClick(bible) }
    }

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        val container = v.findViewById<FrameLayout>(R.id.container_bible_book)
        val bibleBookName = v.findViewById<TextView>(R.id.txt_bible_book)
    }


}