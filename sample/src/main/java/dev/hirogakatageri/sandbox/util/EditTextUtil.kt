package dev.hirogakatageri.sandbox.util

import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.enableMultiLineDoneAction() {
    imeOptions = EditorInfo.IME_ACTION_DONE
    setRawInputType(InputType.TYPE_CLASS_TEXT)
}
