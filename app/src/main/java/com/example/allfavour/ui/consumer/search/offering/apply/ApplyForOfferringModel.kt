package com.example.allfavour.ui.consumer.search.offering.apply

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.databinding.adapters.TimePickerBindingAdapter
import androidx.databinding.library.baseAdapters.BR
import com.example.allfavour.R
import com.google.android.libraries.places.api.model.LocalTime
import com.google.android.libraries.places.api.model.Place
import java.util.*
import java.util.regex.Pattern

class ApplyForOfferringModel : BaseObservable() {

    @Bindable
    var startHour: Int? = null
        set(startHour) {
            field = startHour
            notifyPropertyChanged(BR.valid)
        }

    @Bindable
    var endHour: Int? = null
        set(endHour) {
            field = endHour
            notifyPropertyChanged(BR.valid)
        }

    @Bindable
    var message: String? = null
        set(message) {
            field = message
            notifyPropertyChanged(BR.valid)
        }

    @Bindable
    var date: Long? = null
        set(date) {
            field = date
            notifyPropertyChanged(BR.valid)
        }

    var messageError = ObservableField<Int>()
    var endHourError = ObservableField<String>()

    @Bindable
    var valid: Boolean = false
        get() {
            val messageValid = message != null
            val dateValid = date != null

            return (startEndHourValid(false)
                    && messageValid
                    && dateValid)
        }


    fun startEndHourValid(setMessage: Boolean): Boolean {
        if (startHour == null) {
            return false
        }

        if (endHour == null) {
            return false
        }

        if (startHour!! > endHour!!) {
            if (setMessage) endHourError.set("Start hour must be before end hour !")

            return false
        }

        return true
    }
}