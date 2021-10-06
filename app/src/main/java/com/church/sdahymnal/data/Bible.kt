package com.church.sdahymnal.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.church.sdahymnal.database.Book
import java.io.Serializable

@Entity
data class BibleBook(
        @PrimaryKey
        val id : Long,
        val bookNameEng : String,
        val bookNameMm : String,
        val shortNameMM : String
)


@Entity(foreignKeys = [ForeignKey(entity = BibleBook::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("bookId"),
        onDelete = CASCADE
)])
data class BibleData(
        @PrimaryKey
        val wordId : Long,
        val word : String,
        val verseNumber : Int,
        val chapterNumber : Int,
        val bookId: Long
): Serializable

data class BibleBookNameWithWord(
        val id : Long,
        val bookNameEng : String,
        val bookNameMm : String,
        val bookId : Long,
        val wordId : Long,
        val word : String,
        val verseNumber : Int,
        val chapterNumber : Int
)

data class BibleBookAndChapter(
        val id : Long,
        val bookNameMm : String,
        val bookNameEng : String,
        val totalChapter : Double
)