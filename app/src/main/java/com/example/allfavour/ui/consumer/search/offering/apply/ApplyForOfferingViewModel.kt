package com.example.allfavour.ui.consumer.search.offering.apply

import android.widget.TimePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.allfavour.data.model.ApplicationModel
import com.example.allfavour.data.model.PeriodModel
import java.util.*

class ApplyForOfferingViewModel : ViewModel() {
    lateinit var fields: ApplyForOfferringModel

    private val _application = MutableLiveData<ApplicationModel>()
    val application: LiveData<ApplicationModel> = this._application

    fun init() {
        fields =
            ApplyForOfferringModel()

    }

    fun setDate(epochDay: Long) {
        fields.date = epochDay
    }

    fun onMessageChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        print(s)
    }

    fun onStartHourChanged(view: TimePicker, hourOfDay: Int, minute: Int) {
        val secondsOfDay = (hourOfDay * 60 + minute) * 60
        fields.startHour = secondsOfDay
    }

    fun onEndHourChanged(view: TimePicker, hourOfDay: Int, minute: Int) {
        val secondsOfDay = (hourOfDay * 60 + minute) * 60
        fields.endHour = secondsOfDay
    }

    fun onSubmit() {
        val startCal = Calendar.getInstance()
        startCal.timeInMillis = fields.date!!
        startCal.add(Calendar.SECOND, fields.startHour!!)

        val endCal = Calendar.getInstance()
        endCal.timeInMillis = fields.date!!
        endCal.add(Calendar.SECOND, fields.endHour!!)

        val period = PeriodModel(
            startDate = 0,
            endDate = null,
            startHour = 0, // it will be converted toInput() and the backend will fill this props
            endHour = null,
            startTime = startCal,
            endTime = endCal
        )

        _application.value = ApplicationModel(
            id = null,
            applyTime = Calendar.getInstance().time,
            consumer = null,
            message = fields.message!!,
            time = listOf(period)
        )
    }
}

//
//@BindingAdapter("error")
//fun setError(timePicker: TimePicker, strOrResId: Any?) {
//    if (strOrResId is Int) {
//        timePicker.error = timePicker.context.getString(strOrResId)
//    } else if (strOrResId is String) {
//        timePicker.error = strOrResId
//    }
//
//}
