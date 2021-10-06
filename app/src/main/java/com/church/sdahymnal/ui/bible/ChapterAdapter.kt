package com.church.sdahymnal.ui.bible

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.church.sdahymnal.R
import com.church.sdahymnal.utils.Utils

class ChapterAdapter() : ListAdapter<Int, ChapterAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chapter_number, parent, false)

        return ViewHolder(view)
    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val chapterNumber = v.findViewById<TextView>(R.id.txt_chapter_number)
    }

    override fun onBindViewHolder(holder: ChapterAdapter.ViewHolder, position: Int) {
        val chapter = getItem(position)
        holder.chapterNumber.text = Utils.convertToMyanmarNumber(chapter.toString())
        holder.chapterNumber.setOnClickListener {
            onItemClickListener?.let{
                it(chapter)
            }
        }
    }

    private var onItemClickListener : ((Int) -> Unit)? = null
    fun setOnChapterClickListener(listener : (Int) -> Unit){
        onItemClickListener = listener
    }

}

