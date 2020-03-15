package com.example.allfavour.ui.consumer.search.offering

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.allfavour.DecoratedActivity

import com.example.allfavour.R
import com.google.android.material.datepicker.MaterialDatePicker

class ApplyForOfferingDialog : DialogFragment() {

    companion object {
        fun newInstance() = ApplyForOfferingDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.apply_for_offering_dialog_fragment, container, false)



        return view
    }

    fun setupDatePicker(){
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
            Log.d("DatePicker Activity", "Date String = ${picker.headerText}:: Date epoch value = ${it}")
        }
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
