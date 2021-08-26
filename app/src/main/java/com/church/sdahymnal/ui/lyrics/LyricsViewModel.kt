package com.church.sdahymnal.ui.lyrics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.church.sdahymnal.database.Bookmark
import com.church.sdahymnal.database.Chorus
import com.church.sdahymnal.database.Verses
import com.church.sdahymnal.repository.HymnalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LyricsViewModel @Inject constructor(val app : Application, val repo : HymnalRepository) : AndroidViewModel(app) {

    private var viewModelJob = Job()


    // TODO need to check
    private var songId : Int = 1

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    val versesList = MutableLiveData<List<Verses>>()

    val chorusList = MutableLiveData<List<Chorus>>()

    val insertResult = MutableLiveData<Long>()

    val deleteResult = MutableLiveData<Int>()


    init {
    }

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }

    fun setSongId(id : Int){
        songId = id
        getVerses()
    }

    fun getVerses(){
        uiScope.launch {
            versesList.value = repo.getVerses(songId)
        }
    }

    fun getChorus(){
        uiScope.launch {
            chorusList.value = repo.getChorus(songId)
        }
    }

    fun addToBookmark(bookmark : Bookmark){
        uiScope.launch {
            insertResult.value = repo.addToBookmark(bookmark)
        }
    }

    fun removeFromBookmark(bookmark: Bookmark){
        uiScope.launch {
            deleteResult.value = repo.removeFromBookmark(bookmark)
        }
    }
}
