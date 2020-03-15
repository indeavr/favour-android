package com.example.allfavour.ui

import android.view.View
import androidx.lifecycle.ViewModel
import android.widget.EditText
import android.widget.RadioGroup
import androidx.lifecycle.MutableLiveData
import androidx.databinding.BindingAdapter
import androidx.lifecycle.viewModelScope
import com.example.allfavour.R
import com.example.allfavour.data.ProviderRepository
import com.example.allfavour.data.model.LocationModel
import com.example.allfavour.data.model.ProviderModel
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.launch


class BasicInfoFormViewModel(val providerRepository: ProviderRepository) : ViewModel() {
    lateinit var fields: BasicFormModel
    lateinit var userId: String

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

//    fun onAgeChanged(value: CharSequence, start: Int, before: Int, count: Int) {
//        fields.isAgeValid(true)
//    }

    fun onGenderChanged(radioGroup: RadioGroup, id: Int) {
        when (id) {
            R.id.female_radio -> fields.sex = "Female"
            R.id.male_radio -> fields.sex = "Male"
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

            val provider = ProviderModel(
                id = "",
                phoneNumber = fields.phoneNumber!!,
                location = location,
                sex = fields.sex!!,
                firstName = null,
                lastName = null
            )

            viewModelScope.launch {
                val result = providerRepository.createProvider(userId, provider)

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

