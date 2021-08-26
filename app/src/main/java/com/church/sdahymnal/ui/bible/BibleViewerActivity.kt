package com.church.sdahymnal.ui.bible

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.church.sdahymnal.R
import com.church.sdahymnal.repository.HymnalRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

//@AndroidEntryPoint
//class BibleViewerActivity : AppCompatActivity() {
//
//    private val viewModel : BibleViewerViewModel by viewModels()
//
//    companion object{
//        val KEY_BIBLE = "key_bible"
//    }
//
//    private var bibleId : Int = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bible_viewer)
//
//        intent?.run {
//            bibleId = this.getIntExtra(KEY_BIBLE,0)
//        }
//    }
//}
//
//@HiltViewModel
//class BibleViewerViewModel @Inject constructor(val app: Application, val repo: HymnalRepository) : AndroidViewModel(app) {
//
//    private var viewModelJob = Job()
//
//    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
//
//    val booksList = MutableLiveData<List<BibleBook>>()
//
//    init {
//        getBooks()
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        viewModelJob.cancel()
//    }
//
//    fun getBooks(){
//        uiScope.launch {
//            booksList.value = repo.getBibleBooks()
//        }
//    }
//}