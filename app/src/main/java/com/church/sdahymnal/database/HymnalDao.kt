package com.church.sdahymnal.database

import androidx.room.*
import com.church.sdahymnal.data.BibleBook
import com.church.sdahymnal.data.BibleBookAndChapter
import com.church.sdahymnal.data.BibleBookNameWithWord
import com.church.sdahymnal.data.BibleData

@Dao
interface BookDao {

    @Insert
    fun insertAllBook( books : List<Book>)

    @Query("SELECT * FROM Book ORDER BY bookId ")
    fun getAllBooks() : List<Book>

    @Query("SELECT * FROM Book WHERE bookId=:id ")
    fun getAbook(id: Long) : Book

    @Query("DELETE FROM Book")
    fun clear()

}

@Dao
interface SongDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSong( song: List<Song>)

    @Query("SELECT * FROM Song WHERE id = :id AND songBookId = :bookId")
    fun getSong(id : Long, bookId : Long) : Song

    @Query("SELECT * FROM Song WHERE songBookId = :bookId  ORDER BY songNumber")
    fun getAllSongs(bookId : Long) : List<Song>

    @Query("SELECT * FROM Song WHERE songNumber LIKE :no")
    fun getSongsByNo(no : Long): List<Song>

    @Query("SELECT * FROM Song WHERE songTitle LIKE :text")
    fun getSongsByTitle(text : String) : List<Song>

    @Query("SELECT Song.id, Song.songBookId ,Song.songNumber, Song.songTitle, Song.songTitleEng , Book.bookId ,Book.name, Book.language FROM Song INNER JOIN Book ON Song.songBookId = Book.bookId WHERE Song.id IN (SELECT songId FROM Bookmark)")
    fun getFavouriteSongs() : List<SongWithBookInfo>

    @Query("SELECT Song.id, Song.songBookId ,Song.songNumber, Song.songTitle, Song.songTitleEng , Book.bookId ,Book.name, Book.language FROM Song INNER JOIN Book ON Song.songBookId = Book.bookId ")
    fun getSongWithBookInfo() : List<SongWithBookInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addBookmark(bookmark: Bookmark):Long

    @Delete
    fun removeBookmark(bookmark: Bookmark): Int
//
//    @Query("SELECT * FROM Bookmark")
//    fun getBookmarkSongIds() : List<Bookmark>


    @Query("DELETE FROM Song")
    fun clear()

}

@Dao
interface VerseDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllVerse(verse: List<Verses>)

    @Query("SELECT * FROM Verses WHERE songId = :songId")
    fun getVerses(songId : Long) : List<Verses>

//    @Transaction
//    @Query("SELECT * FROM Verses INNER JOIN Chorus ON Verses.songId = Chorus.songId ")
//    fun getVersesAndChorus(songId : Long) : SongWithVersesAndChorus

    @Query("DELETE FROM Verses")
    fun clear()
}

@Dao
interface ChorusDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChorus(chorus : List<Chorus>)

    @Query("SELECT * FROM Chorus WHERE songId = :songId")
    fun getChorus(songId : Long) : List<Chorus>

    @Query("DELETE FROM Chorus")
    fun clear()
}


@Dao
interface BibleBookDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBibleBook(bibleBook : List<BibleBook>)

    @Query("SELECT * FROM BibleBook")
    fun getAllBible(): List<BibleBook>

    @Query("SELECT * FROM BibleBook WHERE id = :bibleBookId")
    fun getBibleBook(bibleBookId : Long) : BibleBook

    @Query("DELETE FROM BibleBook")
    fun clear()

}

@Dao
interface BibleDataDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBibleData(bibleData : List<BibleData>)

    @Query("SELECT * FROM BibleData WHERE bookId = :bibleBookId")
    fun getBibleByBookId(bibleBookId: Long) : List<BibleData>

    @Query("SELECT * FROM BibleData WHERE bookId= :bookNumber AND chapterNumber = :chapterNumber")
    fun getBibleByChapter(chapterNumber : Int, bookNumber : Long)  : List<BibleData>


    @Query("SELECT BibleBook.id, BibleBook.bookNameEng, BibleBook.bookNameMm, BibleData.bookId, BibleData.wordId, BibleData.word, BibleData.verseNumber, BibleData.chapterNumber  FROM BibleBook INNER JOIN BibleData on BibleBook.id = BibleData.bookId WHERE BibleData.chapterNumber = :chapterNumber AND BibleData.bookId = :bookNumber")
    fun getBibleBookWithWord(chapterNumber : Int, bookNumber : Long) : List<BibleBookNameWithWord>

    @Query("SELECT COUNT(DISTINCT chapterNumber )  FROM BibleData WHERE BibleData.bookId = :bookId  ")
    fun getTotalChapterByBook(bookId : Long) : Int

    @Query("SELECT BibleBook.id, BibleBook.bookNameMm, BibleBook.bookNameEng, COUNT(DISTINCT chapterNumber ) as totalChapter  FROM BibleBook INNER JOIN BibleData on BibleBook.id = BibleData.bookId WHERE BibleData.bookId = :bookId")
    fun getTotalChapterByBookInfo(bookId : Long) : BibleBookAndChapter

}