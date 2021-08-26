package com.church.sdahymnal.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.church.sdahymnal.database.Song
import com.church.sdahymnal.repository.HymnalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val app : Application, val repo: HymnalRepository ) : AndroidViewModel(app) {


    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    val songList = MutableLiveData<List<Song>>()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getSongs(bookId : Int){
        uiScope.launch {
            songList.value = repo.getSongs(bookId)
        }
    }

    fun querySongByNo(no : Int){
        uiScope.launch {
            songList.value = repo.querySongByNumber(no)
        }
    }

    fun querySongByTitle(text : String){
        uiScope.launch {
            songList.value = repo.querySongByTitle(text)
        }
    }

}
