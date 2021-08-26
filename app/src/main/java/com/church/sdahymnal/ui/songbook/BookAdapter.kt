package com.church.sdahymnal.ui.songbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.church.sdahymnal.R
import com.church.sdahymnal.database.Book
import com.church.sdahymnal.utils.RecyclerViewItemClickListener

class BookAdapter(val context : Context,
                  val list : List<Book>,
                  var selectedBookId : Int ,
                  val listener : RecyclerViewItemClickListener)  : RecyclerView.Adapter<BookAdapter.ViewHolder> (){

    var selected_position = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_books, parent, false   )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookAdapter.ViewHolder, position: Int) {
        val book = list[position]
        selected_position = selectedBookId-1

        if(selected_position==position){
            holder.container.isSelected = true
            holder.bookName.setTextColor(context.resources.getColor(R.color.white))
        }else{
            holder.container.isSelected = false
            holder.bookName.setTextColor(context.resources.getColor(R.color.dark_gray))
        }


        holder.bookName.text = book.name
        holder.container.setOnClickListener {
            if(position == RecyclerView.NO_POSITION) return@setOnClickListener
            notifyItemChanged(selected_position)
            selected_position = position
            selectedBookId = selected_position+1
            notifyItemChanged(selected_position)
            listener.onItemClick(book)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val bookName = itemView.findViewById<TextView>(R.id.txt_book_name)
        val container = itemView.findViewById<ViewGroup>(R.id.layout_container)
    }
}