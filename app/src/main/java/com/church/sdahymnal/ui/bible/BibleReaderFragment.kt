package com.church.sdahymnal.ui.bible

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.church.sdahymnal.R
import com.church.sdahymnal.data.BibleData
import com.church.sdahymnal.repository.HymnalRepository
import com.church.sdahymnal.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates


private const val ARG_POSITION = "position"
private const val ARG_LIST = "list"
private const val ARG_BIBLE_ID = "bible_id"

@AndroidEntryPoint
class BibleReaderFragment : Fragment(){

        @Inject lateinit var repo : HymnalRepository
        private var savedFontSize by Delegates.notNull<Float>()
        val adapter by lazy {
            BibleWordAdapter(savedFontSize.toFloat())
        }

        private var recyclerView : RecyclerView? = null

        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)

        private var position: Int? = null
        private var bibleId : Int? = null
//        private var list = MutableLiveData<List<BibleData>>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                position = it.getInt(ARG_POSITION)
//            list =  it.getSerializable(ARG_LIST) as ArrayList<BibleData>
                bibleId = it.getInt((ARG_BIBLE_ID))
            }
            savedFontSize = (Utils.getFromPreference(requireContext(), Utils.KEY_BIBLE_FONT_SIZE, "18f")).toFloat()
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_bible_reader, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            recyclerView = view.findViewById(R.id.rv_book_reader)
            recyclerView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            if(bibleId != null && position!=null){
                getBibleDataByBookIdChapter(bibleId!!, position!!+1)
            }
            recyclerView?.adapter = adapter
        }

        companion object {

        @JvmStatic
        fun newInstance(position : Int, bibleBookId : Int) =
                BibleReaderFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_POSITION, position)
                        putInt(ARG_BIBLE_ID, bibleBookId)
                    }
                }
         }

        fun onLargeFont(){
            savedFontSize += 2f
            adapter.increaseFontSize()
            Utils.saveToPreference(requireActivity(), Utils.KEY_BIBLE_FONT_SIZE, savedFontSize.toString())
        }

        fun onSmallFont(){
            savedFontSize -= 2f
            adapter.decreaseFontSize()
            Utils.saveToPreference(requireActivity(), Utils.KEY_BIBLE_FONT_SIZE, savedFontSize.toString())
        }

        override fun onDestroy() {
            super.onDestroy()
            job.cancel()
        }

        fun getBibleDataByBookIdChapter(bookId : Int , chapterNumber : Int){
            uiScope.launch{
                val result  = (repo.getBibleDataByChapter(bookId.toLong(), chapterNumber)).toMutableList()
                adapter.submitList(result)
            }
        }
}