package com.church.sdahymnal.ui.bible

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.church.sdahymnal.data.BibleBook
import com.church.sdahymnal.data.BibleData
import com.church.sdahymnal.repository.HymnalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BibleTableContentViewModel @Inject constructor(val app: Application, val repo: HymnalRepository) : AndroidViewModel(app) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val booksList = MutableLiveData<List<BibleBook>>()

    val chapterList = MutableLiveData<List<BibleData>>()

    val totalChapter = MutableLiveData<Int>()

    init {
        getBooks()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getBooks(){
        uiScope.launch {
            booksList.value = repo.getBibleBooks()
        }
    }

    fun getChapterList(bookId : Int){
        uiScope.launch {
            chapterList.value = repo.getBibleDataByBookId(bookId.toLong())
        }
    }

    fun getTotalChapter(bookId: Int){
        uiScope.launch {
            totalChapter.value = repo.getTotalChapter(bookId.toLong())
        }
    }
}


