package com.example.allfavour.ui.consumer.basicForm

import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allfavour.R
import com.example.allfavour.data.ConsumerRepository
import com.example.allfavour.data.model.ConsumerModel
import com.example.allfavour.data.model.LocationModel
import com.example.allfavour.data.model.ProviderModel
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.launch

class BasicInfoConsumerFormViewModel(val consumerRepository: ConsumerRepository) : ViewModel() {
    lateinit var fields: BasicFormConsumerModel
    lateinit var userId: String

    lateinit var onFocusPhoneNumber: View.OnFocusChangeListener

    val submittedSuccessfully = MutableLiveData<Boolean>()

    fun init() {
        fields = BasicFormConsumerModel()

        onFocusPhoneNumber = View.OnFocusChangeListener { view, focused ->
            val et = view as EditText
            if (et.text.isNotEmpty() && !focused) {
                fields.isPhoneNumberValid(true)
            }
        }

    }

//    fun onAgeChanged(value: CharSequence, start: Int, before: Int, count: Int) {
//        fields.isAgeValid(true)
//    }

    fun onGenderChanged(radioGroup: RadioGroup, id: Int) {
        when (id) {
            R.id.consumer_female_radio -> fields.sex = "Female"
            R.id.consumer_male_radio -> fields.sex = "Male"
        }
    }

    fun setPlace(place: Place) {
        fields.place = place
    }

    fun onSubmit() {
        if (fields.valid) {
            val place = fields.place!!
            val coordinates = place.latLng!!

            val location = LocationModel(
                id = null,
                mapsId = place.id!!,
                address = place.address!!,
                country = "",
                latitude = coordinates.latitude,
                longitude = coordinates.longitude,
                town = ""
            )

            val consumer = ConsumerModel(
                id = "",
                phoneNumber = fields.phoneNumber!!,
                sex = fields.sex!!,
                firstName = null,
                lastName = null
            )

            viewModelScope.launch {
                val result = consumerRepository.createConsumer(userId, consumer)

                submittedSuccessfully.value = true
            }

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

