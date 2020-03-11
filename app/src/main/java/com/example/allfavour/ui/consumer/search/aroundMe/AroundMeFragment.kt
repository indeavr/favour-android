package com.example.allfavour.ui.consumer.search.aroundMe

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navGraphViewModels
import com.example.allfavour.DecoratedActivity

import com.example.allfavour.R
import com.example.allfavour.data.model.Offering
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModel
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception
import com.google.android.gms.maps.MapFragment


class AroundMeFragment : DialogFragment(), GoogleMap.OnMarkerClickListener,
    OnMapReadyCallback {

    companion object {
        fun newInstance() = AroundMeFragment()
    }

    private val navController: NavController by lazy { NavHostFragment.findNavController(this) }

    private val PERTH = LatLng(-31.952854, 115.857342)
    private val SYDNEY = LatLng(-33.87365, 151.20689)
    private val BRISBANE = LatLng(-27.47093, 153.0235)


    private lateinit var mPerth: Marker
    private lateinit var mSydney: Marker
    private lateinit var mBrisbane: Marker

    private lateinit var mMap: GoogleMap

    private val factory = OfferingsSearchViewModelFactory()
    private val viewModel: OfferingsSearchViewModel by navGraphViewModels(R.id.consumer_search_navigation) { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.offeringsList.observe(this, Observer<ArrayList<Offering>> { offerings ->
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.offerings_around_me_fragment, container, false)

        setupView(view)

        return view
    }

    private fun setupView(view: View) {
        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            dialog!!.dismiss()
        }

        val activity = requireActivity() as DecoratedActivity
        activity.toggleBottomNavVisibility(false)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        navController.popBackStack()
        val activity = requireActivity() as DecoratedActivity
        activity.toggleBottomNavVisibility(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val fm = activity!!.supportFragmentManager
        val frag = fm.findFragmentById(R.id.offerings_map)

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireActivity()

        val mapFragment: SupportMapFragment =
            activity.supportFragmentManager.findFragmentById(R.id.offerings_map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        // Add some markers to the map, and add a data object to each marker.
        mPerth = mMap.addMarker(
            MarkerOptions()
                .position(PERTH)
                .title("Perth")
        )
        mPerth.tag = 0

        mSydney = mMap.addMarker(
            MarkerOptions()
                .position(SYDNEY)
                .title("Sydney")
        )
        mSydney.tag = 0

        mBrisbane = mMap.addMarker(
            MarkerOptions()
                .position(BRISBANE)
                .title("Brisbane")
        )
        mBrisbane.tag = 0

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return true
    }

}
