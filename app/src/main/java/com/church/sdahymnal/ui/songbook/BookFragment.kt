package com.church.sdahymnal.ui.songbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.church.sdahymnal.R
import com.church.sdahymnal.database.Book
import com.church.sdahymnal.databinding.FragmentBookBinding
import com.church.sdahymnal.ui.tableofcontent.SharedViewModel
import com.church.sdahymnal.utils.RecyclerViewItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class BookFragment : Fragment() {

    private lateinit var binding : FragmentBookBinding

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var callback : OnBookFragmentInteractionListener? = null

//    private val bookDatabase by lazy {
//        activity?.let { HymnalDatabase.getInstance(it).bookDao }
//    }
//
//    private val songDatabase by lazy {
//        activity?.let { HymnalDatabase.getInstance(it).songDao }
//    }
//
//    private val verseDatabase by lazy {
//        activity?.let { HymnalDatabase.getInstance(it).versesDao }
//    }
//
//    private val chorusDatabase by lazy {
//        activity?.let { HymnalDatabase.getInstance(it).chorusDao }
//    }


    private val viewModel : SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // layout inflate
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init repository
//        val repo = HymnalRepository(bookDatabase!!, songDatabase!!, verseDatabase!!, chorusDatabase!!)

        prepareRecyclerView()

//        val factory = SharedViewModelFactory(requireActivity().application, repo)
//        viewModel = ViewModelProvider(requireParentFragment(), factory).get(SharedViewModel::class.java)


        viewModel.booksList.observe(viewLifecycleOwner, Observer {

            if(it.isNotEmpty()) viewModel.getSongs(it[0].bookId.toInt())

            binding.rvBook.adapter = BookAdapter(requireActivity(), it, 1 ,object : RecyclerViewItemClickListener {
                override fun onItemClick(obj : Any) {
                    val bookItem = obj as Book
                    viewModel.bookOnClick(bookItem.bookId.toInt())
                }

                override fun onDeleteClick(obj: Any) {

                }
            })
        })
    }

    // Recyclerview Setup

    private fun prepareRecyclerView(){
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        binding.rvBook.addItemDecoration(decoration)
        binding.rvBook.layoutManager = layoutManager
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        try {
//            callback = context as OnBookFragmentInteractionListener
//        } catch (e: ClassCastException) {
//            throw ClassCastException(context.toString() + " must implement OnBookFragmentInteractionListener")
//        }
//
//        if(parentFragment is OnBookFragmentInteractionListener){
//            callback = context as OnBookFragmentInteractionListener
//        }else{
//            throw ClassCastException("$context must implement OnBookFragmentInteractionListener")
//        }
//
//    }

}

interface OnBookFragmentInteractionListener{
    fun onItemClick(book: Book)
}