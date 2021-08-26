package com.church.sdahymnal.repository

import com.church.sdahymnal.data.BibleBook
import com.church.sdahymnal.data.BibleData
import com.church.sdahymnal.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class HymnalRepository @Inject constructor(
    private val bookDao: BookDao,
    private val songDao: SongDao,
    private val verseDao: VerseDao,
    private val chorusDao: ChorusDao,
    private val bibleBookDao : BibleBookDao,
    private val bibleDataDao: BibleDataDao
){

    // retrieve data from database

    suspend fun getBooks() : List<Book>{
        return withContext(Dispatchers.IO){
            val books = bookDao!!.getAllBooks()
            books
        }
    }

    suspend fun getSongs(bookId : Int): List<Song> {
        return withContext(Dispatchers.IO){
            val songs = songDao.getAllSongs(bookId.toLong())
            songs
        }
    }

    suspend fun getVerses(songId : Int) : List<Verses>{
        return withContext(Dispatchers.IO){
            val verses = verseDao.getVerses(songId.toLong())
            verses
        }
    }

    suspend fun getChorus(songId: Int): List<Chorus>{
        return withContext(Dispatchers.IO){
            Timber.tag("song_id").d(songId.toLong().toString())
            val chorus = chorusDao.getChorus(songId.toLong())
            chorus
        }
    }

    suspend fun getBookmarkSong() : List<SongWithBookInfo>{
        return withContext(Dispatchers.IO){
            val songs = songDao.getFavouriteSongs()
            songs
        }
    }

    suspend fun addToBookmark(bookmark : Bookmark): Long{
        return withContext(Dispatchers.IO){
             songDao.addBookmark(bookmark)
        }
    }

    suspend fun removeFromBookmark(bookmark: Bookmark): Int{
        return withContext(Dispatchers.IO){
            songDao.removeBookmark(bookmark)
        }
    }

    suspend fun querySongByNumber(no : Int) : List<Song>{
        return withContext(Dispatchers.IO){
            val songs = songDao.getSongsByNo(no.toLong())
            songs
        }
    }

    suspend fun querySongByTitle(text : String) : List<Song>{
        return withContext(Dispatchers.IO){
            val songs = songDao.getSongsByTitle(text)
            songs
        }
    }

    // add data to database

    suspend fun addBooks(bookList : List<Book>){
        return withContext(Dispatchers.IO){
            bookDao.insertAllBook(bookList)
        }
    }

    suspend fun addSongs(songList : List<Song>){
        return withContext(Dispatchers.IO){
            songDao.insertAllSong(songList)
        }
    }

    suspend fun addVerses(versesList : List<Verses>){
        return withContext(Dispatchers.IO){
            verseDao.insertAllVerse(versesList)
        }
    }

    suspend fun addChorus(chorusList: List<Chorus>){
        return withContext(Dispatchers.IO){
            chorusDao.insertChorus(chorusList)
        }
    }

//     bible

    suspend fun addBibleBooks(bibleBookList: List<BibleBook>){
        return withContext(Dispatchers.IO){
            bibleBookDao.insertBibleBook(bibleBookList)
        }
    }

    suspend fun getBibleBooks() : List<BibleBook>{
        return withContext(Dispatchers.IO){
            bibleBookDao.getAllBible()
        }
    }

    suspend fun addBibleData(bibleData: List<BibleData>){
        return withContext(Dispatchers.IO){
            bibleDataDao.insertBibleData(bibleData)
        }
    }

    suspend fun getBibleDataByBookId(bibleId : Long): List<BibleData> {
        return withContext(Dispatchers.IO){
            bibleDataDao.getBibleByBookId(bibleId)
        }
    }

    suspend fun getBibleDataByChapter(bibleBookId : Long, chapterNumber : Int) : List<BibleData> {
        return withContext(Dispatchers.IO){
            bibleDataDao.getBibleByChapter(chapterNumber, bibleBookId)
        }
    }

    suspend fun getTotalChapter(bibleBookId: Long) : Int{
        return withContext(Dispatchers.IO){
            bibleDataDao.getTotalChapterByBook(bibleBookId)
        }
    }


}