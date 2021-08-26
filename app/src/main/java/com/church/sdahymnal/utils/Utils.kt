package com.church.sdahymnal.utils

import android.content.Context
import android.view.View
import com.church.sdahymnal.data.BibleBook
import com.church.sdahymnal.data.BibleData
//import com.church.sdahymnal.data.BibleBook
import com.church.sdahymnal.database.Book
import com.church.sdahymnal.database.Chorus
import com.church.sdahymnal.database.Song
import com.church.sdahymnal.database.Verses
import com.google.android.material.snackbar.Snackbar
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.io.BufferedReader
import java.io.InputStreamReader

object Utils {
    const val preferenceName = "com.church.sdahymnal"
    const val KEY_FIRST_TIMER_USER = "key_first_time_user"
    const val KEY_SELECTED_BOOK = "key_selected_book"
    const val KEY_DARK_THEME = "key-dark-theme"

    fun readBookCsv(context : Context) : MutableList<Book>{
        val csvParser = CSVParserBuilder().withSeparator('|').withEscapeChar('\n').build()
        val list = mutableListOf<Book>()
        val csvReaderBuilder = CSVReaderBuilder(BufferedReader(InputStreamReader(context.assets.open("books.csv"), "UTF-8")))
                .withCSVParser(csvParser)
                .build()
        val result = csvReaderBuilder.readAll()
        result.forEachIndexed { index, strings ->
            if(index>0){
                list.add(Book(strings[0].trim().toLong(), strings[1], strings[2]))
            }
        }
        return list
    }

    fun readSongCsv(context : Context) : MutableList<Song>{
        val csvParser = CSVParserBuilder().withSeparator('|').withEscapeChar('\n').build()
        val list = mutableListOf<Song>()
        val csvReaderBuilder = CSVReaderBuilder(BufferedReader(InputStreamReader(context.assets.open("songs.csv"), "UTF-8")))
                .withCSVParser(csvParser)
                .build()
        val result = csvReaderBuilder.readAll()
        result.forEachIndexed { index, strings ->
            if(index>0){
                list.add(Song(strings[0].trim().toLong(), strings[1].trim().toLong(), strings[2].trim().toLong(), strings[3], strings[4]))
            }
        }
        return list
    }

    fun readVersesCsv(context : Context) : MutableList<Verses>{
        val csvParser = CSVParserBuilder().withSeparator('|').withEscapeChar('\n').build()
        val list = mutableListOf<Verses>()
        val csvReaderBuilder = CSVReaderBuilder(BufferedReader(InputStreamReader(context.assets.open("verse.csv"), "UTF-8")))
                .withCSVParser(csvParser)
                .build()
        val result = csvReaderBuilder.readAll()
        result.forEachIndexed { index, strings ->
            if(index>0){
                list.add(Verses(strings[0].trim().toLong(), strings[1].trim().toLong(), strings[2].trim().toInt(), strings[3]))

            }
        }
        return list
    }

    fun readBibleBook(context : Context) : MutableList<BibleBook>{
        val csvParser = CSVParserBuilder().withSeparator('|').withEscapeChar('\n').build()
        val list = mutableListOf<BibleBook>()
        val csvReaderBuilder = CSVReaderBuilder(BufferedReader(InputStreamReader(context.assets.open("bible_book.csv"), "UTF-8")))
                .withCSVParser(csvParser)
                .build()
        val result = csvReaderBuilder.readAll()
        result.forEachIndexed { index, strings ->
            if(index>0){
                list.add(BibleBook(strings[0].trim().toLong(), strings[1].toString(), strings[2].toString()))
            }
        }
        return list
    }

    fun readBibleData(context : Context) : MutableList<BibleData>{
        val csvParser = CSVParserBuilder().withSeparator('|').withEscapeChar('\n').build()
        val list = mutableListOf<BibleData>()
        val csvReaderBuilder = CSVReaderBuilder(BufferedReader(InputStreamReader(context.assets.open("bible.csv"), "UTF-8")))
                .withCSVParser(csvParser)
                .build()
        val result = csvReaderBuilder.readAll()
        result.forEachIndexed { index, strings ->
            if(index>0){
                list.add(BibleData(strings[0].trim().toLong(),strings[1].toString(),  strings[2].trim().toInt(), strings[3].trim().toInt(), strings[4].trim().toLong()))
            }
        }
        return list
    }

    fun readChorusCsv(context : Context) : MutableList<Chorus>{
        val csvParser = CSVParserBuilder().withSeparator('|').withEscapeChar('\n').build()
        val list = mutableListOf<Chorus>()
        val csvReaderBuilder = CSVReaderBuilder(BufferedReader(InputStreamReader(context.assets.open("chorus.csv"), "UTF-8")))
                .withCSVParser(csvParser)
                .build()
        val result = csvReaderBuilder.readAll()
        result.forEachIndexed { index, strings ->
            if(index>0){
                list.add(Chorus(strings[0].toLong(), strings[1].toLong(), strings[2].toString()))
            }
        }
        return list
    }

    fun saveToPreference(context: Context, key : String, value : String){
        val pref = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE )
        with(pref.edit()){
            putString(key, value)
            apply()
        }
    }

    fun getFromPreference(context: Context, key: String, defaultString: String) : String{
        val pref = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE )
        val value = pref.getString(key, defaultString).toString()
        return value
    }

    fun isFirstTimeUser(context: Context): Boolean{
        val pref = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_FIRST_TIMER_USER, true)
    }

    fun setFirstTimeUserFalse(context : Context){
        val pref = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE )
        with(pref.edit()){
            putBoolean(KEY_FIRST_TIMER_USER, false)
            apply()
        }
    }

}

fun View.showSnackbar(view: View, message : String){
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}


interface RecyclerViewItemClickListener{
    fun onItemClick(obj : Any)
    fun onDeleteClick(obj: Any)
}