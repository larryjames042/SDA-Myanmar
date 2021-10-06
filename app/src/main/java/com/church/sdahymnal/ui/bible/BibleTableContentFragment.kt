package com.church.sdahymnal.ui.bible

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.church.sdahymnal.R
import com.church.sdahymnal.data.BibleBook
import com.church.sdahymnal.databinding.FragmentBibleTableContentBinding
import com.church.sdahymnal.ui.tableofcontent.SharedViewModel
import com.church.sdahymnal.utils.BibleVerseDialogFragment
import com.church.sdahymnal.utils.ChapterItemClickListener
import com.church.sdahymnal.utils.RecyclerViewItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.properties.Delegates

@AndroidEntryPoint
class BibleTableContentFragment : Fragment(), ChapterItemClickListener, RecyclerViewItemClickListener{

    private val KEY_BIBLE_ID = "key_bible_id"
    private val KEY_BIBLE_NAME = "key_bible_name"
    private lateinit var dialog : BibleVerseDialogFragment
    private val viewModel : BibleTableContentViewModel by viewModels()
    private lateinit var binding : FragmentBibleTableContentBinding
    private var bibleBookId : Int? = null
    private var bibleBookName : String? = null

    private val adapter = BibleBookAdapter(this)




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bible_table_content, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState!=null){
            bibleBookId = savedInstanceState.getInt(KEY_BIBLE_ID)
            bibleBookName = savedInstanceState.getString(KEY_BIBLE_NAME, "")
        }else{
            bibleBookId = 1
            bibleBookName = ""
        }
        prepareRecyclerView()

        viewModel.bibleBooksList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.totalChapter.observe(viewLifecycleOwner, Observer {
            val size = it
            val list = arrayListOf<Int>()
            for(i in 1..size){
                list.add(i)
            }
            dialog =BibleVerseDialogFragment.newInstance(list, this)
            dialog.show(parentFragmentManager, BibleVerseDialogFragment.TAG)

        })
    }

    override fun onResume() {
        super.onResume()
//        if(this::dialog.isInitialized){
//            dialog.setTargetFragment(this)
//        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(bibleBookId !=null && bibleBookName != null){
            outState.putInt(KEY_BIBLE_ID, bibleBookId!!)
            outState.putString(KEY_BIBLE_NAME, bibleBookName!!)
        }

        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        if(this::dialog.isInitialized){
            dialog.dismissAllowingStateLoss()
        }
    }

    private fun prepareRecyclerView(){
        val layoutManager = GridLayoutManager(activity, 4, GridLayoutManager.VERTICAL, false)
        binding.rvBiblebook.layoutManager = layoutManager
        binding.rvBiblebook.adapter = adapter
    }

    override fun onChapterClick(chapterNumber: Int) {
        dialog.dismiss()
        val intent = Intent(activity, BibleViewerActivity::class.java)
        intent.putExtra(BibleViewerActivity.KEY_BOOK_NAME, bibleBookName )
        intent.putExtra(BibleViewerActivity.KEY_BOOK_ID, bibleBookId)
        intent.putExtra(BibleViewerActivity.KEY_CHAPTER_ID, chapterNumber)
        startActivity(intent)
    }

    override fun onItemClick(obj: Any) {
        // get bible chapter list from database

        val bible = obj as BibleBook
        bibleBookId = bible.id.toInt()
        bibleBookName = bible.bookNameMm

        viewModel.getTotalChapter(bible.id.toInt())
    }

    override fun onDeleteClick(obj: Any) {
    }
}