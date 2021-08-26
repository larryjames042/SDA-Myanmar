package com.church.sdahymnal.ui.bible

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.church.sdahymnal.R
import com.church.sdahymnal.data.BibleBook
import com.church.sdahymnal.databinding.FragmentBibleTableContentBinding
import com.church.sdahymnal.utils.BibleVerseDialogFragment
import com.church.sdahymnal.utils.RecyclerViewItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BibleTableContentFragment : Fragment() {

    private val viewModel : BibleTableContentViewModel by activityViewModels()
    private lateinit var binding : FragmentBibleTableContentBinding
    private val adapter by lazy {
        BibleBookAdapter(object : RecyclerViewItemClickListener{
            override fun onItemClick(obj: Any) {
                // get bible chapter list from database

                val bible = obj as BibleBook
                viewModel.getTotalChapter(bible.id.toInt())
            }

            override fun onDeleteClick(obj: Any) {

            }
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bible_table_content, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        viewModel.booksList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.totalChapter.observe(viewLifecycleOwner, Observer {
            val size = it
            val list = arrayListOf<Int>()
            for(i in 1..size){
                list.add(i)
            }
            BibleVerseDialogFragment.newInstance(list).show(parentFragmentManager, BibleVerseDialogFragment.TAG)

        })
    }

    private fun prepareRecyclerView(){
        val layoutManager = GridLayoutManager(activity, 4, GridLayoutManager.VERTICAL, false)
        binding.rvBiblebook.layoutManager = layoutManager
        binding.rvBiblebook.adapter = adapter
    }


}