package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import com.google.android.material.textfield.TextInputLayout
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.findParentOfType
import ru.skillbranch.devintensive.extensions.scrollDownTo

class SmartTextInputLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): TextInputLayout(context, attrs, defStyleAttr) {

    private val scrollView by lazy(LazyThreadSafetyMode.NONE) {
        findParentOfType<ScrollView>() ?: findParentOfType<NestedScrollView>()
    }

    private fun scrollIfNeeded() {
        scrollView?.postDelayed( {
            val errorView = this.findViewById<View>(R.id.textinput_error)
            errorView?.let {
                scrollView?.scrollDownTo(errorView)
            }
        }, 450)
    }

    override fun setError(errorText: CharSequence?) {
        val changed = error != errorText

        super.setError(errorText)

        if (errorText == null) isErrorEnabled = false
        if (changed) scrollIfNeeded()
    }

}