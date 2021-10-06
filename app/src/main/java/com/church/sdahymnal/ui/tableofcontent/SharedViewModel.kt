package com.church.sdahymnal.ui.tableofcontent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.church.sdahymnal.data.BibleBook
import com.church.sdahymnal.data.BibleData
import com.church.sdahymnal.database.Book
import com.church.sdahymnal.database.Song
import com.church.sdahymnal.repository.HymnalRepository
import com.church.sdahymnal.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(val app: Application, val repo: HymnalRepository) : AndroidViewModel(app) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // Songs
    val booksList = MutableLiveData<List<Book>>()

    val songList = MutableLiveData<List<Song>>()

    val bookIdReady = MutableLiveData<Int>()

    val isLoadingDatabase = MutableLiveData<Boolean>()



    init {
        getBooks()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

     fun getBooks(){

        uiScope.launch {
            if(Utils.isFirstTimeUser(app)){
                isLoadingDatabase.value = true
                try{
                    val bookList = Utils.readBookCsv(app)
                    val songList = Utils.readSongCsv(app)
                    val verseList = Utils.readVersesCsv(app)
                    val chorusList = Utils.readChorusCsv(app)
                    val bibleBook = Utils.readBibleBook(app)
                    val bibleData = Utils.readBibleData(app)

                    repo.addBooks(bookList)
                    repo.addSongs(songList)
                    repo.addVerses(verseList)
                    repo.addChorus(chorusList)
                    repo.addBibleBooks(bibleBook)
                    repo.addBibleData(bibleData)


                    Utils.setFirstTimeUserFalse(app)
                    isLoadingDatabase.value = false
                    booksList.value = repo.getBooks()
                }catch (error : Exception){
                    isLoadingDatabase.value = false
                    Timber.tag("csv_read_error").e(error)
                }
            }else{
                booksList.value = repo.getBooks()
            }

        }
    }

    fun bookOnClick(bookId: Int){
        getSongs(bookId)
    }

    fun getSongs( bookId : Int){
        uiScope.launch {
            songList.value = repo.getSongs(bookId)
        }
    }
}
