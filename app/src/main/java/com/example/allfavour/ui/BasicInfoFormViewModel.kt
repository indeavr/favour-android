package com.example.allfavour.ui

import android.view.View
import androidx.lifecycle.ViewModel
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.databinding.BindingAdapter


class BasicInfoFormViewModel : ViewModel() {
    lateinit var fields: BasicFormModel

    lateinit var onFocusPhoneNumber: View.OnFocusChangeListener
    val submittedSuccessfully = MutableLiveData<Boolean>()

    fun init() {
        fields = BasicFormModel()
        onFocusPhoneNumber = View.OnFocusChangeListener { view, focused ->
            val et = view as EditText
            if (et.text.isNotEmpty() && !focused) {
                fields.isPhoneNumberValid(true)
            }
        }
    }

    fun onSubmit() {
        if (fields.valid) {
            // do graphql request then change value to navigate
            submittedSuccessfully.value = true
        }
    }

}

@BindingAdapter("error")
fun setError(editText: EditText, strOrResId: Any?) {
    if (strOrResId is Int) {
        editText.error = editText.context.getString(strOrResId)
    } else if (strOrResId is String) {
        editText.error = strOrResId
    }

}

@BindingAdapter("onFocus")
fun bindFocusChange(editText: EditText, onFocusChangeListener: View.OnFocusChangeListener) {
    if (editText.onFocusChangeListener == null) {
        editText.onFocusChangeListener = onFocusChangeListener
    }
}

