package com.notsatria.storyapp.ui.components

import android.content.Context
import android.graphics.Canvas
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.notsatria.storyapp.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        setError(context.getString(R.string.please_enter_valid_email))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.input_email)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.toString().length <= 1) {
            setError(context.getString(R.string.email_is_required))
        } else if (!text.toString().contains('@')) {
            setError(context.getString(R.string.please_enter_valid_email))
        } else {
            error = null
        }
    }
}