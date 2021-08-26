package com.church.sdahymnal

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.church.sdahymnal.data.BibleBook
import com.church.sdahymnal.data.BibleBookNameWithWord
import com.church.sdahymnal.data.BibleData
import com.church.sdahymnal.database.*
import com.church.sdahymnal.repository.HymnalRepository
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4::class)
//class ExampleInstrumentedTest {
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.church.sdahymnal", appContext.packageName)
//    }
//}

@RunWith(AndroidJUnit4::class)
class ReadWriteTest{
    private lateinit var bookDao : BookDao
    private lateinit var songDao: SongDao
    private lateinit var verseDao: VerseDao
    private lateinit var chorusDao : ChorusDao
    private lateinit var bibleDao : BibleBookDao
    private lateinit var bibleDataDao : BibleDataDao

    lateinit var repo : HymnalRepository

    private lateinit var db : HymnalDatabase

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, HymnalDatabase::class.java).build()
        bookDao = db.bookDao
        songDao = db.songDao
        verseDao  = db.versesDao
        chorusDao = db.chorusDao
        bibleDao = db.bibleBookDao
        bibleDataDao = db.bibleDataDao
        repo = HymnalRepository(bookDao, songDao, verseDao,chorusDao, bibleDao, bibleDataDao)

//        bibleDataDao = db.bibleDataDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun SongBookTest(){
        val book : Book = Book(1, "Myanmar Hymnal", "MM")
        bookDao.insertAllBook(listOf(book))
        val books  = bookDao.getAllBooks()
        assertThat(books[0], equalTo(book))
        val song  = Song(1, 1, 1, "Greate is Thy Faithfulness", "Greate is They Faithfuless")
        val song2  = Song(2, 1, 1, "Greate is Thy Faithfulness", "Greate is They Faithfuless")
        songDao.insertAllSong(listOf(song))
        val songs = songDao.getAllSongs(1)
        assertThat(songs[0], equalTo(song))
    }

    @Test
    @Throws(Exception::class)
    fun BibleBookTest(){
        // Test   BibleBook insert
        val biblebook = BibleBook(1, "Genesis", "Genesis")
        bibleDao.insertBibleBook(listOf(biblebook))
        // Test Biblebook query all
        var bibleBooks = bibleDao.getAllBible()
        assertThat(bibleBooks[0], equalTo(biblebook))
        // Test Biblebook query by id
        var selectedBook = bibleDao.getBibleBook(1)
        assertThat(bibleBooks[0], equalTo(selectedBook))
        // Test Biblebook remove
//        bibleDao.clear()
//        bibleBooks = bibleDao.getAllBible()
//        assertThat(bibleBooks.isEmpty(), equalTo(true))

        // testing Bible Data
        val bibleData = BibleData(1, "In the Beginning God created the heaven and the earth", 1,1, 1)
        bibleDataDao.insertBibleData(listOf(bibleData))
        val bibleAllData = bibleDataDao.getBibleByBookId(1)
        assertThat(bibleAllData[0], equalTo(bibleData))

        val bibleDataByChapter = bibleDataDao.getBibleByChapter(1,1)
        assertThat(bibleDataByChapter[0], equalTo(bibleData))

       val bibleBookInfoWithData = bibleDataDao.getBibleBookWithWord(1,1)
        val sampleDate = BibleBookNameWithWord(1, biblebook.bookNameEng, biblebook.bookNameMm, bibleData.bookId, bibleData.wordId, bibleData.word, bibleData.verseNumber, bibleData.chapterNumber)
        assertThat(bibleBookInfoWithData[0], equalTo(sampleDate))

        val totalChapterInABook = bibleDataDao.getTotalChapterByBook(1)
        assertThat(totalChapterInABook, equalTo(1))
    }

}