package com.church.sdahymnal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.church.sdahymnal.data.BibleBook
import com.church.sdahymnal.data.BibleData

@Database(entities = [Book::class, Song::class, Verses::class, Chorus::class, Bookmark::class, BibleBook::class, BibleData::class], version = 1, exportSchema = false)
abstract class HymnalDatabase  : RoomDatabase(){
    abstract val bookDao : BookDao
    abstract val songDao : SongDao
    abstract val versesDao : VerseDao
    abstract val chorusDao : ChorusDao
    abstract val bibleBookDao : BibleBookDao
    abstract val bibleDataDao : BibleDataDao

    companion object{
        @Volatile
        private var INSTANCE: HymnalDatabase? = null

        fun getInstance(context: Context): HymnalDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HymnalDatabase::class.java,
                        "hymnal_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}