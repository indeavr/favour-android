package com.example.allfavour.ui.provider.basicForm

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.allfavour.MainNavigationDirections

import com.example.allfavour.R
import com.example.allfavour.databinding.BasicInfoFormFragmentBinding
import androidx.lifecycle.Observer
import com.example.allfavour.DecoratedActivity
import com.example.allfavour.ui.auth.AuthViewModelFactory
import com.example.allfavour.ui.auth.AuthenticationViewModel
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class BasicInfoFormFragment : DialogFragment() {
    private val apiKey: String by lazy { getString(R.string.google_api_key) }

    val mainNavController: NavController? by lazy { activity?.findNavController(R.id.main_nav_activity) }

    private val viewModel: BasicInfoFormViewModel by lazy {
        ViewModelProviders.of(
            this,
            BasicInfoFormViewModelFactory()
        ).get(BasicInfoFormViewModel::class.java)
    }

    private val authViewModel: AuthenticationViewModel by lazy {
        ViewModelProviders.of(
            this.requireActivity(),
            AuthViewModelFactory()
        ).get(AuthenticationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(
                requireActivity().applicationContext,
                apiKey
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: BasicInfoFormFragmentBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.basic_info_form_fragment,
                container,
                false
            )

        viewModel.init()
        viewModel.userId = authViewModel.userId // AuthenticationProvider.getUserId(requireActivity())!!

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        setupSubmit()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGoogleLocationAutocomplete()
    }

    private fun setupSubmit() {
        viewModel.submittedSuccessfully.observe(viewLifecycleOwner, Observer<Boolean> {
            val action = MainNavigationDirections.providerSearchDest()
            mainNavController?.navigate(action)
        })
    }

    fun setupGoogleLocationAutocomplete() {
        val autocompleteFragment =
            activity!!.supportFragmentManager.findFragmentById(R.id.maps_autocomplete_fragment_basic_forms) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.ID,
                Place.Field.NAME
            )
        )

//        autocompleteFragment
//            .setTypeFilter(TypeFilter.ADDRESS)

        autocompleteFragment
            .setCountry("BG")

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                viewModel.setPlace(place)
            }

            override fun onError(status: Status) {

            }
        })
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
        val frag = fm.findFragmentById(R.id.maps_autocomplete_fragment_basic_forms)

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

    companion object {
        fun newInstance() =
            BasicInfoFormFragment()
    }
}
