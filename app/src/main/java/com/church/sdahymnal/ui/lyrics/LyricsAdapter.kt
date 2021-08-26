package com.church.sdahymnal.ui.lyrics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.church.sdahymnal.R


class LyricsAdapter(val list : List<Pair<String, String>>,private var fontSize : Float = 16f) : RecyclerView.Adapter<LyricsAdapter.ViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lyrics, parent, false   )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LyricsAdapter.ViewHolder, position: Int) {
        val (a,b) = list[position]
        if(a.equals("Refrain") || a.equals("ထပ်ဆို")){
            holder.number.setTextColor(holder.itemView.resources.getColor(R.color.dark_orange))
        }else{
            holder.number.setTextColor(holder.itemView.resources.getColor(R.color.lyrics_color))
        }
        holder.number.text = a
        holder.lyrics.text = b
        holder.number.textSize = fontSize
        holder.lyrics.textSize = fontSize
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun increaseFontSize(){
        fontSize+= 2f
        notifyDataSetChanged()
    }

    fun decreaseFontSize(){
        fontSize-= 2f
        notifyDataSetChanged()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val number = itemView.findViewById<TextView>(R.id.txt_number)
        val lyrics = itemView.findViewById<TextView>(R.id.txt_lyrics)
    }
}