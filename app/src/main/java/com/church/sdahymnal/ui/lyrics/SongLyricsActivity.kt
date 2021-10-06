package com.church.sdahymnal.ui.lyrics

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.church.sdahymnal.R
import com.church.sdahymnal.database.*
import com.church.sdahymnal.databinding.ActivitySongLyricsBinding
import com.church.sdahymnal.repository.HymnalRepository
import com.church.sdahymnal.utils.Utils
import com.church.sdahymnal.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.properties.Delegates
import kotlin.text.StringBuilder

@AndroidEntryPoint
class SongLyricsActivity : AppCompatActivity() {

    companion object{
        const val KEY_SONG = "song_id"
    }

    private val  viewModel : LyricsViewModel by viewModels()

    lateinit var binding : ActivitySongLyricsBinding


    private var verseList : List<Verses>? = null
    private var chorusList : List<Chorus>? = null

    private var adapter : LyricsAdapter? = null

    private var song : Song? = null

    private var fontSize  by Delegates.notNull<Float>()

    private val list by lazy {
        mutableListOf<Pair<String, String>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_song_lyrics)


        prepareRecyclerView()

        intent?.run{
            song = this.getSerializableExtra(KEY_SONG) as Song
        }

        song?.run {
            viewModel.setSongId(song!!.id.toInt())
        }

        viewModel.versesList.observe(this, Observer {
            verseList = it
            viewModel.getChorus()
        })

        viewModel.insertResult.observe(this, Observer {
            Timber.tag("insertresult").d(it.toString())
            if (it.toInt() == -1) {
                // already exist in database so.. remove it
                val bookmark = Bookmark(songId = song!!.id)
                viewModel.removeFromBookmark(bookmark)

            } else {
                // success .. added to bookmark
                binding.mainLayout.apply {
                    showSnackbar(this, "Added to Favourite")
                }
            }
        })

        viewModel.deleteResult.observe(this, Observer {
            if (it == 1) {
                binding.mainLayout.apply {
                    showSnackbar(this, "Removed from Favourite!")
                }
            } else {
                binding.mainLayout.apply {
                    showSnackbar(this, "Fail to Delete!")
                }
            }

        })

        viewModel.chorusList.observe(this, Observer {
            binding.txtSongNumber.text = "${Utils.convertToMyanmarNumber(song!!.songNumber.toString())}."
            binding.txtSongTitle.text = song!!.songTitle.toString()
            binding.txtSongTitleEng.text = song!!.songTitleEng.toString()

            chorusList = it

            Timber.tag("chorus_size").d(it?.size.toString())

            var i = 0
            list.clear()
            while (i < verseList!!.size) {
                list.add(Pair((i + 1).toString(), verseList!![i].verseContent))
                if(chorusList?.size!=0 && i==0){  // chorus is only added once right after verse number 1
                    if (!chorusList!![0].chorus.equals("na")) {
                        if(song?.songBookId?.toInt()==2){   // Check the language
                            list.add(Pair("ထပ်ဆို", chorusList!![0].chorus))
                        }else{
                            list.add(Pair("Refrain", chorusList!![0].chorus))
                        }

                    }
                }
                i++

            }
            fontSize = (Utils.getFromPreference(this, Utils.KEY_SONG_FONT_SIZE, "18f")).toFloat()
            adapter = LyricsAdapter(list, fontSize)
            binding.rvLyrics.adapter = adapter

        })

        clickListener()
    }

    private fun clickListener(){
        binding.imgBookmark.setOnClickListener {
            val bookmark = Bookmark(songId = song!!.id)
            viewModel.addToBookmark(bookmark)
        }

        binding.imgZoomin.setOnClickListener {
            adapter?.increaseFontSize()
            fontSize+=2f
            Utils.saveToPreference(this, Utils.KEY_SONG_FONT_SIZE, fontSize.toString())
        }

        binding.imgZoomout.setOnClickListener {
            adapter?.decreaseFontSize()
            fontSize-=2f
            Utils.saveToPreference(this, Utils.KEY_SONG_FONT_SIZE, fontSize.toString())
        }

        binding.imgCopy.setOnClickListener {
            copyText(listToReadableText(list))
            binding.mainLayout.apply {
                showSnackbar(this, "Copied!")
            }
        }

        binding.imgShare.setOnClickListener {
            shareText(listToReadableText(list))
        }
    }

    private fun listToReadableText(list : MutableList<Pair<String,String>>) : String{
        var stringBuilder = StringBuilder()
        stringBuilder.append(song!!.songTitle.plus("\n\n"))
        list.map {
            stringBuilder
                    .append(it.first)
                    .append(". ")
                    .append(it.second)
                    .append("\n\n")
        }
        return stringBuilder.toString()
    }

    private fun copyText(text: String){
        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("lyrics", text)
        clipboard.setPrimaryClip(clip)
    }

    private fun shareText(text: String){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The title")
        startActivity(Intent.createChooser(shareIntent, "Share via.."))
    }


    private fun prepareRecyclerView(){
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvLyrics.layoutManager = layoutManager
    }

}