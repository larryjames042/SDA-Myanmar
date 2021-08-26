package com.church.sdahymnal.di

import android.content.Context
import com.church.sdahymnal.database.*
import com.church.sdahymnal.repository.HymnalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext app : Context) = HymnalDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideBookDao(db : HymnalDatabase) = db.bookDao

    @Singleton
    @Provides
    fun provideSongDao(db : HymnalDatabase) = db.songDao

    @Singleton
    @Provides
    fun provideVerseDao(db : HymnalDatabase) = db.versesDao

    @Singleton
    @Provides
    fun provideChorusDao(db : HymnalDatabase) = db.chorusDao

    @Singleton
    @Provides
    fun provideBibleBookDao(db : HymnalDatabase) = db.bibleBookDao

    @Singleton
    @Provides
    fun provideBibleDataDao(db : HymnalDatabase) = db.bibleDataDao


    @Singleton
    @Provides
    fun provideRepository(bookDao: BookDao,
                          chorusDao: ChorusDao,
                          songDao: SongDao,
                          verseDao: VerseDao,
                          bibleBookDao : BibleBookDao,
                          bibleDataDao : BibleDataDao
    ) = HymnalRepository(bookDao, songDao, verseDao, chorusDao, bibleBookDao, bibleDataDao)

}