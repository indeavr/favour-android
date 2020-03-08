package com.example.allfavour.ui.provider.addFavour

import android.app.Dialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.allfavour.DecoratedActivity

import com.example.allfavour.R
import com.example.allfavour.data.model.Favour
import com.example.allfavour.ui.auth.AddFavourViewModelFactory
import kotlinx.android.synthetic.main.provider_add_favour_fragment.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.view.Window
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*


class AddFavourFragment : DialogFragment() {

    companion object {
        fun newInstance() = AddFavourFragment()
    }

    private val navController: NavController by lazy { NavHostFragment.findNavController(this) }
    private val apiKey: String by lazy { getString(R.string.google_api_key) }
    private val placesClient: PlacesClient by lazy { Places.createClient(this.context!!) }

    private val viewModel: AddFavourViewModel by lazy {
        ViewModelProviders.of(
            this,
            AddFavourViewModelFactory()
        ).get(AddFavourViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(
                requireActivity().applicationContext,
                apiKey
            )
        }

        viewModel.status.observe(this, Observer<String> {
            if (it == "success") {
                dialog!!.dismiss()
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.provider_add_favour_fragment, container, false)

        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            dialog!!.dismiss()
        }

        val activity = requireActivity() as DecoratedActivity
        activity.toggleBottomNavVisibility(false)

        return view
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val autocompleteFragment =
            activity!!.supportFragmentManager.findFragmentById(R.id.maps_autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ADDRESS,
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

            }

            override fun onError(status: Status) {

            }
        })



        submit_add_favour.setOnClickListener {
            val title = title_favour_input.editText!!.text.toString()
            val description = description_favour_input.editText!!.text.toString()
            val money = money_favour_input.editText!!.text.toString().toDouble()
            val adress = adress_favour_input.editText!!.text.toString()

            val geocoder = Geocoder(this.context)

            val addresses: List<Address> = geocoder.getFromLocationName(adress, 1)

            if (addresses.isNotEmpty()) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude
            }

            val favour = Favour(null, title, description, money)
            viewModel.addFavour(favour)
        }
    }
}
