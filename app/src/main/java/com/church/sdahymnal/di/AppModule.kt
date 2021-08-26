package com.church.sdahymnal.di

import android.content.Context
import com.church.sdahymnal.MyApplication
import com.church.sdahymnal.database.BookDao
import com.church.sdahymnal.database.ChorusDao
import com.church.sdahymnal.database.SongDao
import com.church.sdahymnal.database.VerseDao
import com.church.sdahymnal.repository.HymnalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context) = app as MyApplication

}