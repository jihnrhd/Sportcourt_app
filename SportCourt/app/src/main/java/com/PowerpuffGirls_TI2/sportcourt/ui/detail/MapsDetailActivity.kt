package com.PowerpuffGirls_TI2.sportcourt.ui.detail

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.PowerpuffGirls_TI2.sportcourt.R
import com.PowerpuffGirls_TI2.sportcourt.databinding.ActivityMapsDetailBinding
import com.PowerpuffGirls_TI2.sportcourt.model.Lapangan
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import retrofit2.Call
import retrofit2.Response

class MapsDetailActivity : AppCompatActivity() {
    companion object {
        private const val ICON_ID = "ICON_ID"
        const val EXTRA_LAPANGAN = "extra lapangan"
    }

    private lateinit var binding: ActivityMapsDetailBinding
    private lateinit var mapboxMap: MapboxMap
    private lateinit var symbolManager: SymbolManager
    private lateinit var locationComponent: LocationComponent
    private lateinit var myLocation: LatLng
    private lateinit var permissionManager: PermissionsManager
    private lateinit var navigationMapRoute: NavigationMapRoute
    private var currentRoute: DirectionsRoute? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        binding = ActivityMapsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                symbolManager = SymbolManager(binding.mapView, mapboxMap, style)
                symbolManager.iconAllowOverlap = true

                style.addImage(
                    ICON_ID,
                    BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_default)
                )

                navigationMapRoute = NavigationMapRoute(
                    null,
                    binding.mapView,
                    mapboxMap,
                    R.style.NavigationMapRoute
                )
                showLapanganLocation()
                showMyLocation(style)
                addMarkerOnClick()
                showNavigation()

            }
        }
    }

    private fun initTitle(title: String) {
        supportActionBar?.title = "Lokasi $title"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showLapanganLocation() {
        val extraLapangan = intent.getParcelableExtra<Lapangan>(EXTRA_LAPANGAN)
        if (extraLapangan != null) {
            initTitle(extraLapangan.nama.toString())
            val latitude = extraLapangan.lat?.toDouble()
            val longitude = extraLapangan.lang?.toDouble()
            val location = LatLng(latitude!!, longitude!!)

            symbolManager.create(
                SymbolOptions()
                    .withLatLng(LatLng(location.latitude, location.longitude))
                    .withIconImage(ICON_ID)
                    .withIconSize(1.5f)
                    .withIconOffset(arrayOf(0f, -1.5f))
                    .withTextField(extraLapangan.nama)
                    .withTextHaloColor("rgba(255,255,255,100)")
                    .withTextHaloWidth(5.0f)
                    .withTextAnchor("top")
                    .withTextOffset(arrayOf(0f, 1.5f))
                    .withDraggable(true)
            )

            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 8.0))
        }


    }


    @SuppressLint("MissingPermission")
    private fun showMyLocation(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponentOptions = LocationComponentOptions.builder(this)
                .pulseEnabled(true)
                .pulseColor(Color.BLUE)
                .pulseAlpha(.4f)
                .pulseInterpolator(BounceInterpolator())
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions
                .builder(this, style)
                .locationComponentOptions(locationComponentOptions)
                .build()

            locationComponent = mapboxMap.locationComponent
            locationComponent.activateLocationComponent(locationComponentActivationOptions)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS

            myLocation = LatLng(
                locationComponent.lastKnownLocation?.latitude as Double,
                locationComponent.lastKnownLocation?.longitude as Double
            )
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12.0))
        } else {
            permissionManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
                    Toast.makeText(
                        this@MapsDetailActivity,
                        "Anda harus mengizinkan location permission untuk menggunakan aplikasi ini",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        mapboxMap.getStyle { style ->
                            showMyLocation(style)
                        }
                    } else {
                        finish()
                    }
                }
            })

            permissionManager.requestLocationPermissions(this)
        }

    }

    private fun addMarkerOnClick() {
        mapboxMap.addOnMapClickListener { point ->
            symbolManager.deleteAll()

            symbolManager.create(
                SymbolOptions()
                    .withLatLng(LatLng(point.latitude, point.longitude))
                    .withIconImage(ICON_ID)
                    .withDraggable(true)
            )


            val destination = Point.fromLngLat(point.longitude, point.latitude)
            val origin = Point.fromLngLat(myLocation.longitude, myLocation.latitude)
            requestRoute(origin, destination)


            binding.btnNavigation.visibility = View.VISIBLE
            true
        }
    }

    private fun requestRoute(origin: Point, destination: Point) {
        navigationMapRoute.updateRouteVisibilityTo(false)
        NavigationRoute.builder(this)
            .accessToken(getString(R.string.access_token))
            .origin(origin)
            .destination(destination)
            .build()
            .getRoute(object : retrofit2.Callback<DirectionsResponse> {
                override fun onResponse(
                    call: Call<DirectionsResponse>,
                    response: Response<DirectionsResponse>
                ) {
                    if (response.body() == null) {
                        Toast.makeText(
                            this@MapsDetailActivity,
                            "Tidak ada rute cek token",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    } else if (response.body()?.routes()?.size == 0) {
                        Toast.makeText(
                            this@MapsDetailActivity,
                            "Tidak ada rute ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    currentRoute = response.body()?.routes()?.get(0)
                    navigationMapRoute.addRoute(currentRoute)
                }

                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    Toast.makeText(this@MapsDetailActivity, "Error : $t", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun showNavigation() {
        binding.btnNavigation.setOnClickListener {
            val simulateRoute = true

            val options = NavigationLauncherOptions.builder()
                .directionsRoute(currentRoute)
                .shouldSimulateRoute(simulateRoute)
                .build()

            NavigationLauncher.startNavigation(this, options)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}