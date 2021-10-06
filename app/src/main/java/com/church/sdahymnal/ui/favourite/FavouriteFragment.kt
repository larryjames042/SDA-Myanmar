package com.church.sdahymnal.ui.favourite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.church.sdahymnal.R
import com.church.sdahymnal.database.HymnalDatabase
import com.church.sdahymnal.database.Song
import com.church.sdahymnal.database.SongWithBookInfo
import com.church.sdahymnal.repository.HymnalRepository
import com.church.sdahymnal.ui.lyrics.SongLyricsActivity
import com.church.sdahymnal.utils.RecyclerViewItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private var rvBookmark : RecyclerView? = null

    private var imgEmpty : ImageView? = null


    private val viewModel : FavouriteViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bookmark, container, false)
        rvBookmark = view.findViewById(R.id.rv_bookmark)
        imgEmpty = view.findViewById(R.id.img_empty)
        prepareRecyclerView()

        viewModel.getSongs()


        val adapter = FavouriteAdapter(object : RecyclerViewItemClickListener {
            override fun onItemClick(obj: Any) {
                val songWithBookInfo = obj as SongWithBookInfo
                val song = Song(
                        songWithBookInfo.id,
                        songWithBookInfo.songBookId,
                        songWithBookInfo.songNumber,
                        songWithBookInfo.songTitle,
                        songWithBookInfo.songTitleEng
                )
                val i = Intent(activity, SongLyricsActivity::class.java)
                i.putExtra(SongLyricsActivity.KEY_SONG, song )
                startActivity(i)
            }

            override fun onDeleteClick(obj: Any) {
                val song = obj as SongWithBookInfo
                viewModel.removeFavourite(song)
            }
        })

        viewModel.favRemoveResult.observe(viewLifecycleOwner, Observer {
            if(it==1){
                viewModel.getSongs()
            }else{

            }

        })

        viewModel.songList.observe(viewLifecycleOwner, Observer {
            if(it.size>0){
                rvBookmark?.visibility = View.VISIBLE
                imgEmpty?.visibility = View.GONE
                adapter.submitList(it)
            }else{
                imgEmpty?.visibility = View.VISIBLE
                rvBookmark?.visibility = View.GONE
            }

        })

        rvBookmark?.adapter  = adapter

        return view
    }

    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun prepareRecyclerView(){
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvBookmark?.layoutManager = layoutManager
    }

    companion object {
      @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookmarkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}