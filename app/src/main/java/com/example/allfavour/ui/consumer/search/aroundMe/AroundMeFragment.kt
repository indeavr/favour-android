package com.example.allfavour.ui.consumer.search.aroundMe

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
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
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.offering_bottom_sheet_fragment.*
import java.text.DecimalFormat
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt


class AroundMeFragment : DialogFragment(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private val TAG: String = AroundMeFragment::class.java.simpleName

    companion object {
        fun newInstance() = AroundMeFragment()
    }

    private val navController: NavController by lazy { NavHostFragment.findNavController(this) }
    private val factory = OfferingsSearchViewModelFactory()
    private val viewModel: OfferingsSearchViewModel by navGraphViewModels(R.id.consumer_search_navigation) { factory }

    private lateinit var map: GoogleMap
    private lateinit var cameraPosition: CameraPosition

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient

    // retrieved by the Fused Location Provider
    private var lastKnownLocation: Location? = null

    // (Sofia) when location permission is not granted.
    private val defaultLocation: LatLng = LatLng(0.0, 0.0)

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: Int = 1
    private val RC_LOCATION_ENABLE: Int = 7
    private val DEFAULT_ZOOM: Float = 10.0f

    private val locationRequest: LocationRequest by lazy {
        LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private var isLocationPermissionGranted: Boolean = false

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val markerToOfferring = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.offerings_around_me_fragment, container, false)

        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet))

        initMaps()

        setupView(view)

        return view
    }

    fun initMaps() {
        if (!Places.isInitialized()) {
            Places.initialize(
                requireActivity().applicationContext,
                getString(R.string.google_api_key)
            )
        }
        val activity = this.requireActivity()

        placesClient = Places.createClient(activity)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        val mapFragment: SupportMapFragment =
            activity.supportFragmentManager.findFragmentById(R.id.offerings_map) as SupportMapFragment

        mapFragment.getMapAsync(this)
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

    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap
        this.map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(this.context, R.raw.map_styles_json)
        )

        this.map.setOnMarkerClickListener(this)
//        this.map.moveCamera(CameraUpdateFactory.newLatLng())

        getLocationPermission()

        updateLocationUI()

        enableLocationSetting()

        attachBottomSheetCurrentOfferingListener()
    }

    private fun updateLocationUI() {
        if (this.map == null) {
            return
        }

        try {
            if (this.isLocationPermissionGranted) {
                this.map.isMyLocationEnabled = true
                this.map.uiSettings.isMyLocationButtonEnabled = true

                subscribeForOfferrings()
            } else {
                this.map.isMyLocationEnabled = false
                this.map.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null

                // Prompt the user for permission.
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }

    }

    private fun subscribeForOfferrings() {
        viewModel.offeringsList.observe(this, Observer<ArrayList<Offering>> { offerings ->
            offerings.forEach {
                val coordinates = LatLng(
                    it.location!!.latitude,
                    it.location!!.longitude
                )

                val marker = map.addMarker(
                    MarkerOptions()
                        .position(coordinates)
                        .icon(bitmapDescriptorFromVector(this.context!!, R.drawable.ic_pet_store))
                )

                markerToOfferring[marker.id] = it.id!!
            }

            viewModel.setCurrentOffering(offerings[0].id!!)
        })
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        try {
            if (this.isLocationPermissionGranted) {
                val locationResult = this.fusedLocationProviderClient.lastLocation

                locationResult.addOnCompleteListener {
                    if (it.isSuccessful) {
                        lastKnownLocation = it.result
                        if (lastKnownLocation != null) {
                            this.map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ),
                                    DEFAULT_ZOOM
                                )
                            )
                        } else {

                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", it.exception)

                        this.map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                defaultLocation,
                                DEFAULT_ZOOM
                            )
                        )
                        this.map.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }

    private fun enableLocationSetting() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(false)

        val client: SettingsClient = LocationServices.getSettingsClient(this.requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // location is turned on
            startLocationUpdates()
        }

        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    startIntentSenderForResult(
                        it.resolution.intentSender,
                        RC_LOCATION_ENABLE,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_LOCATION_ENABLE) {
            if (resultCode == RESULT_OK) {
                startLocationUpdates()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnSuccessListener {
            getDeviceLocation()
        }
    }


    private fun getLocationPermission() {
        /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        val activity = this.requireActivity()

        val hasLocationPersmission = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasLocationPersmission) {
            isLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        isLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    isLocationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    override fun onMyLocationButtonClick(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMyLocationClick(p0: Location) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMarkerClick(marker: Marker?): Boolean {

        val markerId = marker!!.id

        val offeringId = markerToOfferring[markerId]

        if (offeringId != null) {
            viewModel.setCurrentOffering(offeringId)
        }

//        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
////            btn_bottom_sheet.setText("Close sheet");
//        } else {
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
////            btn_bottom_sheet.setText("Expand sheet");
//        }

//        val bottomSheetFragment = OfferingBottomSheet()
//        bottomSheetFragment.show(
//            this.requireActivity().supportFragmentManager,
//            bottomSheetFragment.tag
//        );

        return true
    }

    fun attachBottomSheetCurrentOfferingListener() {
        viewModel.currentOffering.observe(viewLifecycleOwner, Observer<Offering> {
            if (it != null) {
                offering_sheet_title.text = it.title
                offering_sheet_address.text = it.location!!.address

                if (lastKnownLocation != null) {
                    val currentLatLng =
                        LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
                    val markerLatLng = LatLng(it.location!!.latitude, it.location!!.longitude)

                    val distance = floatArrayOf(1f)
                    Location.distanceBetween(
                        lastKnownLocation!!.latitude, lastKnownLocation!!.longitude,
                        it.location!!.latitude, it.location!!.longitude, distance
                    )

                    offering_sheet_distance.text = distance[0].roundToInt().toString() + " m"

                }
            }
        })

    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes
        vectorDrawableResourceId: Int
    ): BitmapDescriptor {
        val background: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_map_pin)!!

        background.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)

        val vectorDrawable: Drawable =
            ContextCompat.getDrawable(context, vectorDrawableResourceId)!!

        vectorDrawable.setBounds(
            40,
            20,
            vectorDrawable.intrinsicWidth + 40,
            vectorDrawable.intrinsicHeight + 20
        )

        val bitmap: Bitmap = Bitmap.createBitmap(
            background.intrinsicWidth,
            background.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        );
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
