package com.code.runner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import com.code.runner.R
import android.graphics.Color

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val scrollView = ScrollView(context)
        scrollView.setBackgroundColor(Color.BLACK)
        
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setBackgroundColor(Color.BLACK)

        val editText = EditText(context)
        editText.hint = "Enter text here"
        editText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
        editText.minLines = 3
        editText.maxLines = Integer.MAX_VALUE
        editText.setPadding(16, 16, 16, 16)
        editText.isVerticalScrollBarEnabled = true
        editText.setScroller(android.widget.Scroller(context))
        editText.isScrollContainer = true
        editText.setBackgroundColor(Color.BLACK)
        editText.setTextColor(Color.WHITE)

        linearLayout.addView(editText)
        scrollView.addView(linearLayout)

        return scrollView
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}