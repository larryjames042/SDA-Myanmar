package com.church.sdahymnal.utils

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.church.sdahymnal.R

class TextSizeDialogFragment(val callback : TextSizeChangeListener) : DialogFragment() {

    companion object{

        const val KEY_CHAPTER_LIST = "key_chapter"
        const val TAG  = " dialog_dag"
        fun newInstance(ls : TextSizeChangeListener) : TextSizeDialogFragment{
            val dialogFragment = TextSizeDialogFragment(ls)
            return dialogFragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.layout_textsize_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bigText = view.findViewById<ImageView>(R.id.img_textsize_increase)
        val smallText = view.findViewById<ImageView>(R.id.img_textsize_decrease)
        bigText.setOnClickListener {
            callback.onBigText()

        }

        smallText.setOnClickListener {
            callback.onSmallText()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

}

interface TextSizeChangeListener{
    fun onBigText()
    fun onSmallText()
}