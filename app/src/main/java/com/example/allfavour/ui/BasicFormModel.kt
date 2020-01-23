package com.example.allfavour.ui

import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.allfavour.R
import java.util.regex.Pattern


class BasicFormModel : BaseObservable() {

    @Bindable
    var firstName: String? = null
        set(firstName) {
            field = firstName
            notifyPropertyChanged(BR.valid)
        }

    @Bindable
    var lastName: String? = null
        set(lastName) {
            field = lastName
            notifyPropertyChanged(BR.valid)
        }

    @Bindable
    var phoneNumber: String? = null
        set(phoneNumber) {
            field = phoneNumber
            notifyPropertyChanged(BR.valid)
        }

    var phoneNumberError = ObservableField<Int>()

    @Bindable
    var valid: Boolean = false
        get() {
            var valid = isPhoneNumberValid(false)
            return valid
        }

    fun isPhoneNumberValid(setMessage: Boolean): Boolean {
        if (phoneNumber.isNullOrBlank()) {
            phoneNumberError.set(null)
            return false
        }

        val isValid = Pattern.matches("^[+]?[0-9]{10,13}\$", phoneNumber)

        if (isValid && setMessage) {
            phoneNumberError.set(null)
        } else {
            phoneNumberError.set(R.string.phoneNumber_wrong)
        }

        return isValid
    }
}