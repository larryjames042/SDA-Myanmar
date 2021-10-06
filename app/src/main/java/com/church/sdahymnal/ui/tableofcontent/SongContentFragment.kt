
package com.church.sdahymnal.ui.tableofcontent

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.church.sdahymnal.R
import com.church.sdahymnal.database.Book
import com.church.sdahymnal.database.Song
import com.church.sdahymnal.databinding.FragmentContentBinding
import com.church.sdahymnal.ui.lyrics.SongLyricsActivity
import com.church.sdahymnal.ui.search.SearchActivity
import com.church.sdahymnal.utils.RecyclerViewItemClickListener
import com.church.sdahymnal.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_BOOKID = "bookId"

@AndroidEntryPoint
class SongContentFragment : Fragment() {

    lateinit var binding : FragmentContentBinding

    private var bookId: String? = null

    private val viewModel : SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookId  = it.getString(ARG_BOOKID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        // progress dialog
        val progressDialog = ProgressDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Please Wait!")
        progressDialog.setMessage("Setting up database")

        viewModel.isLoadingDatabase.observe(viewLifecycleOwner, Observer {
            if(it){
                progressDialog.show()
            }else{
                progressDialog.dismiss()
            }
        })

        val adapter = SongListAdapter( object : RecyclerViewItemClickListener{
            override fun onItemClick(obj: Any) {
                val item = obj as Song
                val i = Intent(activity, SongLyricsActivity::class.java)
                i.putExtra(SongLyricsActivity.KEY_SONG, item )
                startActivity(i)
            }

            override fun onDeleteClick(obj: Any) {

            }
        })

        viewModel.booksList.observe(viewLifecycleOwner, Observer {
            val selectedBook = Utils.getFromPreference(requireContext(), Utils.KEY_SELECTED_BOOK, "2")

            if(it.isNotEmpty()) viewModel.getSongs(selectedBook.toInt())

            binding.rvBook.adapter = BookAdapter(requireActivity(), it,selectedBook.toInt(), object : RecyclerViewItemClickListener {
                override fun onItemClick(obj : Any) {
                    val bookItem = obj as Book
                    bookId = bookItem.bookId.toString()
                    viewModel.bookOnClick(bookItem.bookId.toInt())
                    Utils.saveToPreference(requireContext(), Utils.KEY_SELECTED_BOOK, bookItem.bookId.toString())
                }

                override fun onDeleteClick(obj: Any) {

                }
            })
        })

        viewModel.songList.observe(viewLifecycleOwner, Observer {

            adapter.submitList(it)

            if(it.isNotEmpty()){
                bookId = it[0].songBookId.toString()
            }
        })
        binding.rvContent?.adapter  = adapter

        binding.fabNumber?.setOnClickListener {

            val i = Intent(requireActivity(), SearchActivity::class.java)
            i.putExtra(SearchActivity.KEY_BOOK, bookId)
            i.putExtra(SearchActivity.KEY_SEARCH_TYPE, SearchActivity.NUMBER)
            startActivity(i)
        }

        binding.fab?.setOnClickListener {
            val i = Intent(requireActivity(), SearchActivity::class.java)
            i.putExtra(SearchActivity.KEY_BOOK, bookId)
            i.putExtra(SearchActivity.KEY_SEARCH_TYPE, SearchActivity.TEXT)
            startActivity(i)
        }
    }

    private fun prepareRecyclerView(){
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvContent?.layoutManager = layoutManager
        val horizontalLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvBook.layoutManager = horizontalLayoutManager
    }



    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            SongContentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_BOOKID, param1)
                }
            }
    }
}