package com.church.sdahymnal.ui.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.church.sdahymnal.database.Bookmark
import com.church.sdahymnal.database.Song
import com.church.sdahymnal.database.SongWithBookInfo
import com.church.sdahymnal.repository.HymnalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(val app: Application, val repo: HymnalRepository) : AndroidViewModel(app) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val songList = MutableLiveData<List<SongWithBookInfo>>()

    val favRemoveResult = MutableLiveData<Int>()

    init {
//        getSongs()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getSongs(){
        uiScope.launch {
            songList.value = repo.getBookmarkSong()
        }
    }

    fun removeFavourite(song : SongWithBookInfo){
        uiScope.launch {
            val song = Bookmark(songId = song.id)
            favRemoveResult.value = repo.removeFromBookmark(song)
        }
    }
}

