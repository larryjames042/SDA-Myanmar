package com.church.sdahymnal.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.church.sdahymnal.R
import com.church.sdahymnal.database.Song
import com.church.sdahymnal.database.HymnalDatabase
import com.church.sdahymnal.databinding.ActivitySearchBinding
import com.church.sdahymnal.repository.HymnalRepository
import com.church.sdahymnal.ui.lyrics.SongLyricsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() , OnItemClickListener{

    companion object{
       const val KEY_BOOK = "key_book"
        const val KEY_SEARCH_TYPE = "key_search_type"
        const val NUMBER = "number"
        const val TEXT = "text"
    }

    lateinit var binding : ActivitySearchBinding

    private val viewModel : SearchViewModel by viewModels()




    private var book : String? = null

    private var searchType : String? = null

    private val adapter by lazy {
        SearchAdapter(this)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        intent?.run{
            book = this.getStringExtra(KEY_BOOK).toString()
            searchType = this.getStringExtra(KEY_SEARCH_TYPE).toString()
        }

        book?.run {
            viewModel.getSongs(book!!.toInt())
        }


        prepareRecyclerView()


        binding.searchView.isActivated = true
        binding.searchView.queryHint = "Search song"
        if(searchType.equals(NUMBER)){
            binding.searchView.inputType = InputType.TYPE_CLASS_NUMBER
        }else{
            binding.searchView.inputType = InputType.TYPE_CLASS_TEXT
        }
//
        binding.searchView.onActionViewExpanded()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        viewModel.songList.observe(this, Observer {
            adapter.submitList(it)
        })

    }

    private fun prepareRecyclerView(){
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvQueryResult.layoutManager = layoutManager
        binding.rvQueryResult.adapter = adapter
    }

    override fun onItemClick(song : Song) {
        val i = Intent(this, SongLyricsActivity::class.java)
        i.putExtra(SongLyricsActivity.KEY_SONG, song)
        startActivity(i)
        finish()
    }
}