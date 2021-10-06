package com.church.sdahymnal.ui.bible

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.church.sdahymnal.R
import com.church.sdahymnal.data.BibleData
import com.church.sdahymnal.utils.Utils
import timber.log.Timber

class BibleWordAdapter(private var fontSize : Float = 16f) :
        ListAdapter<BibleData, BibleWordAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BibleData>(){
            override fun areItemsTheSame(oldItem: BibleData, newItem: BibleData): Boolean {
                return oldItem.bookId == newItem.bookId
            }

            override fun areContentsTheSame(oldItem: BibleData, newItem: BibleData): Boolean {
                return  oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BibleWordAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bible_reader, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BibleWordAdapter.ViewHolder, position: Int) {
        val bibleWord = getItem(position)
        val verseNumber = Utils.convertToMyanmarNumber(bibleWord.verseNumber.toString())
        val spannableString = SpannableString("${verseNumber}. ${bibleWord.word}")
        spannableString.setSpan(
            ForegroundColorSpan(Color.RED),
            0,
            bibleWord.verseNumber.toString().length+1,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        holder.bibleVerse.text = spannableString
        holder.bibleVerse.textSize = fontSize
        holder.container.setOnClickListener {

        }
    }

    fun increaseFontSize(){
        fontSize+= 2f
        Timber.tag("onlargefont").d("$fontSize")
        notifyDataSetChanged()
    }

    fun decreaseFontSize(){
        fontSize-= 2f
        notifyDataSetChanged()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val bibleVerse = itemView.findViewById<TextView>(R.id.txt_word)
        val container = itemView.findViewById<FrameLayout>(R.id.container)

    }
}