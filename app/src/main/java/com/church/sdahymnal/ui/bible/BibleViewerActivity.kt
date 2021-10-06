package com.church.sdahymnal.ui.bible

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.church.sdahymnal.R
import com.church.sdahymnal.data.BibleBookAndChapter
import com.church.sdahymnal.data.BibleData
import com.church.sdahymnal.databinding.ActivityBibleViewerBinding
import com.church.sdahymnal.repository.HymnalRepository
import com.church.sdahymnal.utils.TextSizeChangeListener
import com.church.sdahymnal.utils.TextSizeDialogFragment
import com.church.sdahymnal.utils.Utils
import com.church.sdahymnal.utils.findCurrentFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs
import kotlin.properties.Delegates

@AndroidEntryPoint
class BibleViewerActivity : AppCompatActivity() {

    enum class SWIPE {
        NORMAL_SWIPE,
        REVERSE_SWIPE
    }

    private val viewModel : BibleViewerViewModel by viewModels()
    lateinit var binding : ActivityBibleViewerBinding
    lateinit var adapter : BibleViewPagerAdapter

    companion object{
        val KEY_BOOK_ID = "key_book_id"
        val KEY_CHAPTER_ID = "key_chapter_id"
        val KEY_BOOK_NAME = "key_book_name"

    }

    private var bibleId : Int = 1
    private var chapterId : Int = 1
    private var totalChapter by Delegates.notNull<Int>()
    private var bookName : String = ""
    private var viewPagerPosition by Delegates.notNull<Int>()
    private var isPageEnd : Boolean = false
    private var isPageStart : Boolean = false
    private var swipeStatus = SWIPE.NORMAL_SWIPE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bible_viewer)
        intent?.run {
            bibleId = this.getIntExtra(KEY_BOOK_ID, 1)
            chapterId = this.getIntExtra(KEY_CHAPTER_ID, 1)
            bookName = this.getStringExtra(KEY_BOOK_NAME).toString()
            viewModel.getChapterCount(bibleId)
            viewModel.setChapterNumber(chapterId-1)
            binding.txtBibleBook.text = bookName
        }

        binding.pager.offscreenPageLimit = 3

        viewModel.totalChapter.observe(this, Observer { totalChapters ->
            totalChapter = totalChapters.totalChapter.toInt()
            binding.txtBibleBook.text = totalChapters.bookNameMm

            adapter = BibleViewPagerAdapter(supportFragmentManager, lifecycle, totalChapter, bibleId)
            binding.pager.adapter = adapter
            TabLayoutMediator(binding.tapLayout, binding.pager) { tab, position ->
                tab.text = Utils.convertToMyanmarNumber((position + 1).toString())
            }.attach()
            if (swipeStatus == SWIPE.NORMAL_SWIPE) {
                // if swipe normal .. position starts at or user selected position)
                binding.pager.currentItem = viewModel.getChapterNumber()
            } else {
                binding.pager.currentItem = totalChapter - 1
            }
//            isPageStart = chapterId-1 == 0
            viewModel.setChapterNumber(0)
            if (totalChapter - 1 == adapter.itemCount) isPageEnd = true
        })

        onClick()

        val xList = mutableListOf<Float>()
        var x1 : Float = 0f
        var x2 : Float = 0f
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                viewPagerPosition = position
                // check viewpager swipe page end
                Timber.tag("position").d("${position} ${totalChapter-1}")
                if (position == 0 && position == totalChapter - 1) {
                    // reset swipe status in this case.
                    binding.pager.getChildAt(0).setOnTouchListener(object : View.OnTouchListener {
                        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                                when (p1?.action) {

                                    MotionEvent.ACTION_MOVE -> {
                                        Timber.tag("motionswipe").d("${p1.toString()} ${p1?.x}")
                                        xList.add(p1.x)
                                        return true
                                    }
                                    MotionEvent.ACTION_UP -> {
                                        if(xList.size != 0){
                                            val deltaX = xList[xList.size-1] - xList[0]
                                            if(abs(deltaX) > 100){
                                                if(xList[0] < xList[xList.size-1]){
                                                    // left to right
                                                        isPageEnd = false
                                                        isPageStart = true
                                                        bibleId -= 1
                                                        swipeStatus = SWIPE.REVERSE_SWIPE
                                                        viewModel.getChapterCount(bibleId)
                                                         binding.pager.getChildAt(0).setOnTouchListener(null)
                                                        Timber.tag("swipeposition").d("left to right")
                                                    return true
                                                }else{
                                                    // right to left
                                                        bibleId += 1
                                                        swipeStatus = SWIPE.NORMAL_SWIPE
                                                        viewModel.getChapterCount(bibleId)
                                                        isPageEnd = true
                                                        isPageStart = false
                                                        binding.pager.getChildAt(0).setOnTouchListener(null)
                                                        Timber.tag("swipeposition").d("right to left")
                                                    return true
                                                }
                                                xList.clear()
                                            }else{
                                                return false
                                            }

                                        }else{
                                            return false
                                        }
                                    }
                                }
                            return false
                        }
                    })
                } else {
                    if (position == totalChapter - 1 && positionOffset.toString() == (0.0).toString() && positionOffsetPixels == 0) {
                        if (isPageEnd && bibleId < 66) {
                            // increase bible book id
                            bibleId += 1
                            swipeStatus = SWIPE.NORMAL_SWIPE
                            viewModel.getChapterCount(bibleId)
                        }
                    } else if (position == 0 && positionOffset.toString() == (0.0).toString() && positionOffsetPixels == 0) {
                        if (isPageStart && bibleId > 1) {
                            bibleId -= 1
                            swipeStatus = SWIPE.REVERSE_SWIPE
                            viewModel.getChapterCount(bibleId)
                        }
                    } else {
                        isPageEnd = false
                        isPageStart = false
                        swipeStatus = SWIPE.NORMAL_SWIPE
                    }
                }

                Timber.tag("pager_scroll").d("${position} : ${positionOffset}  : ${positionOffsetPixels}")
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    isPageEnd = viewPagerPosition == totalChapter - 1
                    isPageStart = viewPagerPosition == 0
                }
            }
        })
    }

    fun onClick(){
        binding.txtBibleBook.setOnClickListener {
            finish()
        }

        binding.imgTextSize.setOnClickListener{
            val dialogFragment = TextSizeDialogFragment.newInstance(object : TextSizeChangeListener{
                override fun onBigText() {
                    val fragment = binding.pager.findCurrentFragment(supportFragmentManager) as BibleReaderFragment
                    fragment.onLargeFont()

                }

                override fun onSmallText() {
                    val fragment = binding.pager.findCurrentFragment(supportFragmentManager) as BibleReaderFragment
                    fragment.onSmallFont()
                }

            }).show(supportFragmentManager, "Dialog_tag")
        }

    }

    override fun onPause() {
        super.onPause()
        Timber.tag("lifecycle").d("pause")
    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        Timber.tag("lifecycle").d("onResume")
    }

    override fun onStop() {
        super.onStop()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        Timber.tag("lifecycle").d("onStop")
    }

    override fun onStart() {
        super.onStart()
        Timber.tag("lifecycle").d("onStart")
    }
}

@HiltViewModel
class BibleViewerViewModel @Inject constructor(val app: Application, val repo: HymnalRepository) : AndroidViewModel(app) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val totalChapter = MutableLiveData<BibleBookAndChapter>()

    val bibleWordList = MutableLiveData<List<BibleData>>()

    private var chapterId = 1

    init {

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getWordsByChapter(bookId: Int, chapterId: Int){
        uiScope.launch {
            bibleWordList.value = repo.getBibleDataByChapter(bookId.toLong(), chapterId)
        }
    }

    fun setChapterNumber(number : Int){
        chapterId = number
    }

    fun getChapterNumber() = chapterId

    fun getChapterCount(bookId: Int){
        uiScope.launch {
            totalChapter.value = repo.getTotalChapterWithBookInfo(bookId.toLong())
        }
    }
}