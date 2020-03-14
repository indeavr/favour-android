package com.example.allfavour.ui

import androidx.databinding.ObservableField
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.allfavour.R
import com.google.android.libraries.places.api.model.Place
import java.util.regex.Pattern


class BasicFormModel : BaseObservable() {

    @Bindable
    var sex: String? = null
        set(sex) {
            field = sex
            notifyPropertyChanged(BR.valid)
        }

    @Bindable
    var place: Place? = null
        set(place) {
            field = place
            notifyPropertyChanged(BR.valid)
        }

    @Bindable
    var phoneNumber: String? = null
        set(phoneNumber) {
            field = phoneNumber
            notifyPropertyChanged(BR.valid)
        }

    var ageError = ObservableField<Int>()
    var phoneNumberError = ObservableField<Int>()

    @Bindable
    var valid: Boolean = false
        get() {
            val placeValid = place != null
            val sexValid = sex != null

            return (isPhoneNumberValid(false)
                    && sexValid
                    && placeValid)
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

//    fun isAgeValid(setMessage: Boolean): Boolean {
//        if (age == null) {
//            return false
//        }
//
//        if (age!! < 0 || age!! > 100) {
//            if (setMessage) ageError.set(R.string.age_wrong)
//
//            return false
//        }
//
//        return true
//    }
}