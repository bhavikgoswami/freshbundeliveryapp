package com.greypixstudio.broovisdeliveryapp.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText


class LicenceWatcher(private val edTxt: EditText) : TextWatcher {
    private var isDelete = false
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun beforeTextChanged(
        s: CharSequence, start: Int, count: Int,
        after: Int
    ) {
    }

    override fun afterTextChanged(s: Editable) {
        if (isDelete) {
            isDelete = false
            return
        }
        var `val` = s.toString()
        var a = ""
        var b = ""
        var c = ""
        if (`val`.isNotEmpty()) {
            `val` = `val`.replace(" ", "")
            if (`val`.length >= 4) {
                a = `val`.substring(0, 3)
            } else if (`val`.length < 3) {
                a = `val`.substring(0, `val`.length)
            }
            if (`val`.length >= 6) {
                b = `val`.substring(3, 6)
                c = `val`.substring(6, `val`.length)
            } else if (`val`.length in 4..5) {
                b = `val`.substring(3, `val`.length)
            }
            val stringBuffer = StringBuffer()
            if (a.isNotEmpty()) {
                stringBuffer.append(a)
                if (a.length == 3) {
                    stringBuffer.append(" ")
                }
            }
            if (b.isNotEmpty()) {
                stringBuffer.append(b)
                if (b.length == 3) {
                    stringBuffer.append(" ")
                }
            }
            if (c.isNotEmpty()) {
                stringBuffer.append(c)
            }
            edTxt.removeTextChangedListener(this)
            edTxt.setText(stringBuffer.toString())
            edTxt.setSelection(edTxt.text.toString().length)
            edTxt.addTextChangedListener(this)
        } else {
            edTxt.removeTextChangedListener(this)
            edTxt.setText("")
            edTxt.addTextChangedListener(this)
        }
    }

    companion object {
        private val TAG = LicenceWatcher::class.java
            .simpleName
    }

    init {
        edTxt.setOnKeyListener { v, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                isDelete = true
            }
            false
        }
    }
}