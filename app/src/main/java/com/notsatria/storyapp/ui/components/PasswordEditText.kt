package com.notsatria.storyapp.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.notsatria.storyapp.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.input_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        background = ContextCompat.getDrawable(
            context,
            if (focused) R.drawable.edit_text_focused else R.drawable.edit_text_border
        )
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.toString().isEmpty()) {
            setError(context.getString(R.string.password_is_required), null)
        } else if (text.toString().length < 8) {
            setError(context.getString(R.string.password_length_is_not_enough), null)
        } else {
            error = null
        }
    }

}