package com.depromeet.housekeeper.util


import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import timber.log.Timber

object EditTextUtil {
    const val textPattern = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|ㆍᆢ| ]*"

    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(context: Context, editText: EditText){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
    }

    fun EditText.listenEditorDoneAction(onDone: (text: String) -> Unit) {
        var handled = false
        this.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Timber.d("setOnEditorActionListener: ${textView.text}, $actionId, $keyEvent")
                onDone(textView.text.toString())
                this.text.clear()
                this.isFocusable = false
                this.isCursorVisible = false
                handled = true
            }
            handled
        }
        if (handled) return
    }
}