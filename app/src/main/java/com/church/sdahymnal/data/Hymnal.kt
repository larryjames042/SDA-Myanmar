package com.church.sdahymnal.database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.io.Serializable

@Entity
data class Book(
    @PrimaryKey
    val bookId : Long,
    val name : String,
    val language : String
)

@Entity(foreignKeys = [ForeignKey(entity = Book::class,
    parentColumns = arrayOf("bookId"),
    childColumns = arrayOf("songBookId"),
    onDelete = CASCADE)]
)
data class Song(
    @PrimaryKey
    val id : Long,
    val songBookId : Long,
    val songNumber : Long,
    val songTitle : String,
    val songTitleEng : String
) : Serializable

data class SongWithBookInfo(
        val id : Long,
        val songBookId: Long,
        val songNumber : Long,
        val songTitle : String,
        val songTitleEng : String,
        val bookId : Long,
        val name : String,
        val language : String
)

@Entity(foreignKeys = [ForeignKey(entity = Song::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("songId"),
        onDelete = CASCADE)])
data class Bookmark( // favourite
        @PrimaryKey()
        val songId: Long
)

@Entity(foreignKeys = [ForeignKey(entity = Song::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("songId"),
        onDelete = CASCADE)])
data class Verses(
        @PrimaryKey(autoGenerate = true)
    val id: Long,
    val songId : Long,
    val verseNumber : Int,
    val verseContent : String
)

@Entity(foreignKeys = [ForeignKey(entity = Song::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("songId"),
        onDelete = CASCADE)])
data class Chorus(
        @PrimaryKey(autoGenerate = true)
    val id: Long,
    var songId : Long,
    val chorus : String
)


