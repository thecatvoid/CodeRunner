package com.code.runner.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.code.runner.R

class Editor : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.editor, container, false)

        // Find the EditText by its ID
        val editText: EditText = view.findViewById(R.id.editText)

        // Handle tapping anywhere inside the EditText to position the cursor
        editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val x = event.x.toInt()
                val y = event.y.toInt()
                val offset = editText.getOffsetForPosition(x.toFloat(), y.toFloat())
                editText.setSelection(offset)
            }
            false
        }

        return view
    }

    companion object {
        fun newInstance() = Editor()
    }
}
