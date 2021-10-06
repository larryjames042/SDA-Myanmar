package com.church.sdahymnal.utils

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.church.sdahymnal.R
import com.church.sdahymnal.ui.bible.ChapterAdapter

class BibleVerseDialogFragment() : DialogFragment() {

    lateinit var listener : ChapterItemClickListener

    private val chapterAdapter by lazy {
        ChapterAdapter()
    }



    companion object{

        const val KEY_CHAPTER_LIST = "key_chapter"
        const val TAG  = " dialog_dag"
        fun newInstance(list : ArrayList<Int>, chapterClick : ChapterItemClickListener) : BibleVerseDialogFragment{
            val arg  = Bundle()
            arg.putIntegerArrayList(KEY_CHAPTER_LIST, list)
            val dialogFragment = BibleVerseDialogFragment()
            dialogFragment.listener = chapterClick
            dialogFragment.arguments = arg
            return dialogFragment
        }



    }

    fun setTargetFragment(l : ChapterItemClickListener){
        listener = l
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.layout_chapter_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chapterList = arguments?.getIntegerArrayList(KEY_CHAPTER_LIST)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_chapter)
        recyclerView.layoutManager = GridLayoutManager(activity, 6, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = chapterAdapter
        chapterList?.run {
            chapterAdapter.submitList(this)
        }
        chapterAdapter.setOnChapterClickListener {
            listener.onChapterClick(it)
            dismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dismiss()
    }

    override fun onDetach() {
        super.onDetach()
    }
}

interface ChapterItemClickListener{
    fun onChapterClick(chapterNumber : Int)
}




