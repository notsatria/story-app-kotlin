package com.notsatria.storyapp.ui.components

import android.content.Context
import android.graphics.Canvas
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.notsatria.storyapp.R

class DescriptionEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {
    init {
        inputType = InputType.TYPE_CLASS_TEXT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.add_description)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.toString().isEmpty()) {
            setError(context.getString(R.string.description_is_required), null)
        } else {
            error = null
        }
    }
}