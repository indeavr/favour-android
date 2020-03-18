package com.example.allfavour.ui.consumer.search.offering.apply

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.example.allfavour.DecoratedActivity

import com.example.allfavour.databinding.ApplyForOfferingDialogFragmentBinding
import com.example.allfavour.services.authentication.AuthenticationProvider
import com.example.allfavour.ui.auth.AuthViewModelFactory
import com.example.allfavour.ui.auth.AuthenticationViewModel
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModel
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.apply_for_offering_dialog_fragment.*
import android.app.TimePickerDialog
import com.example.allfavour.R
import java.util.*


class ApplyForOfferingDialog : DialogFragment() {

    companion object {
        fun newInstance() =
            ApplyForOfferingDialog()
    }

    private val factory = OfferingsSearchViewModelFactory()
    private val viewModelOffering: OfferingsSearchViewModel by navGraphViewModels(R.id.consumer_search_navigation) { factory }

    private val viewModelApply: ApplyForOfferingViewModel by lazy {
        ViewModelProviders.of(this).get(ApplyForOfferingViewModel::class.java)
    }

    private val viewModelAuth: AuthenticationViewModel by lazy {
        ViewModelProviders.of(this, AuthViewModelFactory())
            .get(AuthenticationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelApply.application.observe(this, Observer { application ->
            //  viewModelOffering.appliedSuccessfully.observe()

            viewModelOffering.applyForOffering(
                AuthenticationProvider.getUserId(requireActivity())!!,
                application
            )
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ApplyForOfferingDialogFragmentBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.apply_for_offering_dialog_fragment,
                container,
                false
            )

        viewModelApply.init()

        binding.lifecycleOwner = this
        binding.viewmodel = viewModelApply

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            dialog!!.dismiss()
        }

        offering_apply_dates_button.setOnClickListener {
            showDatePicker()
        }

        offering_apply_startHour_button.setOnClickListener {
            showStartHourTimePicker()
        }

        offering_apply_endHour_button.setOnClickListener {
            showEndHourTimePicker()
        }
    }

    fun showDatePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.show(requireActivity().supportFragmentManager, picker.toString())

        picker.addOnCancelListener {
            Log.d("DatePicker Activity", "Dialog was cancelled")
        }

        picker.addOnNegativeButtonClickListener {
            Log.d("DatePicker Activity", "Dialog Negative Button was clicked")
        }

        picker.addOnPositiveButtonClickListener {
            offering_apply_dates_button.text = picker.headerText
            if (it != null) {
                viewModelApply.setDate(it)
            }
        }
    }

    fun showStartHourTimePicker() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(
            this.context,
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                viewModelApply.onStartHourChanged(timePicker, selectedHour, selectedMinute)
                offering_apply_startHour_button.text = "Start Time: $selectedHour:$selectedMinute"
            }, hour, minute, true
        )//Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    fun showEndHourTimePicker() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(
            this.context,
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                viewModelApply.onEndHourChanged(timePicker, selectedHour, selectedMinute)
                offering_apply_endHour_button.text = "End Time: $selectedHour:$selectedMinute"
            }, hour, minute, true
        )//Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val activity = requireActivity() as DecoratedActivity
        activity.toggleBottomNavVisibility(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val fm = activity!!.supportFragmentManager
        val frag = fm.findFragmentById(R.id.maps_autocomplete_fragment)

        if (frag != null)
            fm.beginTransaction().remove(frag).commit()
    }

    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT

            dialog!!.window!!.setLayout(width, height)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.setWindowAnimations(R.style.AllFavour_SlideDialog)
        }

    }
}
