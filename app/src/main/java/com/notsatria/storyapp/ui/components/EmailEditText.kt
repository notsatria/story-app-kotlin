package com.notsatria.storyapp.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.notsatria.storyapp.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        setErrorMessage(context.getString(R.string.please_enter_valid_email))
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
            setErrorMessage(context.getString(R.string.email_is_required))
        }
        if (!text.toString().contains('@')) {
            setErrorMessage(context.getString(R.string.please_enter_valid_email))
        }
        else {
            error = null
        }
    }

    private fun setErrorMessage(message: String) {
        (parent as TextInputLayout).error = message
    }
}