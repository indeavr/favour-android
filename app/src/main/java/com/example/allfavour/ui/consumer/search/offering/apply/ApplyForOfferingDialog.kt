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

import com.example.allfavour.R
import com.example.allfavour.databinding.ApplyForOfferingDialogFragmentBinding
import com.example.allfavour.services.authentication.AuthenticationProvider
import com.example.allfavour.ui.auth.AuthViewModelFactory
import com.example.allfavour.ui.auth.AuthenticationViewModel
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModel
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.apply_for_offering_dialog_fragment.*

class ApplyForOfferingDialog : DialogFragment() {

    companion object {
        fun newInstance() =
            ApplyForOfferingDialog()
    }

    private val factory = OfferingsSearchViewModelFactory()
    private val offeringViewModel: OfferingsSearchViewModel by navGraphViewModels(R.id.consumer_search_navigation) { factory }

    private val applyFormViewModel: ApplyForOfferingViewModel by lazy {
        ViewModelProviders.of(this).get(ApplyForOfferingViewModel::class.java)
    }

    private val authViewModel: AuthenticationViewModel by lazy {
        ViewModelProviders.of(this, AuthViewModelFactory())
            .get(AuthenticationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyFormViewModel.application.observe(this, Observer { application ->
            offeringViewModel.applyForOffering(
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

        applyFormViewModel.init()

        binding.lifecycleOwner = this
        binding.viewmodel = applyFormViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offering_apply_dates_button.setOnClickListener {
            showDatePicker()
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
                applyFormViewModel.setDate(it)
            }
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
