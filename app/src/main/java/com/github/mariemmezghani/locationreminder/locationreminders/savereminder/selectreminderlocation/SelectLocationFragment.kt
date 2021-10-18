package com.github.mariemmezghani.locationreminder.locationreminders.savereminder.selectreminderlocation

import com.github.mariemmezghani.locationreminder.R
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.github.mariemmezghani.locationreminder.base.BaseFragment
import com.github.mariemmezghani.locationreminder.databinding.FragmentSelectLocationBinding
import com.github.mariemmezghani.locationreminder.locationreminders.savereminder.SaveReminderViewModel
import com.github.mariemmezghani.locationreminder.utils.setDisplayHomeAsUpEnabled
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

 class SelectLocationFragment : BaseFragment(), OnMapReadyCallback {


     //Use Koin to get the view model of the SaveReminder
     override val _viewModel: SaveReminderViewModel by inject()
     private lateinit var binding: FragmentSelectLocationBinding
     private lateinit var mMap: GoogleMap

     override fun onCreateView(
         inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
     ): View? {
         binding =
             DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false)

         binding.viewModel = _viewModel
         binding.lifecycleOwner = this

         setHasOptionsMenu(true)
         setDisplayHomeAsUpEnabled(true)

//        TODO: add the map setup implementation
         // Get the SupportMapFragment and request notification when the map is ready to be used.
         val mapFragment =
             getChildFragmentManager().findFragmentById(R.id.map) as? SupportMapFragment
         mapFragment?.getMapAsync(this)
//        TODO: zoom to the user location after taking his permission
//        TODO: add style to the map
//        TODO: put a marker to location that the user selected


//        TODO: call this function after the user confirms on the selected location
         onLocationSelected()

         return binding.root
     }

     private fun onLocationSelected() {
         //        TODO: When the user confirms on the selected location,
         //         send back the selected location details to the view model
         //         and navigate back to the previous fragment to save the reminder and add the geofence

     }

     override fun onMapReady(googleMap: GoogleMap) {
         mMap = googleMap

         // Add a marker in Sydney and move the camera
         val sydney = LatLng(-34.0, 151.0)
         mMap.addMarker(
             MarkerOptions()
                 .position(sydney)
                 .title("Marker in Sydney")
         )
         mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
     }

     override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         inflater.inflate(R.menu.map_options, menu)
     }

     override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
         R.id.normal_map -> {
             mMap.mapType=GoogleMap.MAP_TYPE_NORMAL
             true
         }
         R.id.hybrid_map -> {
             mMap.mapType=GoogleMap.MAP_TYPE_HYBRID
             true
         }
         R.id.satellite_map -> {
             mMap.mapType=GoogleMap.MAP_TYPE_SATELLITE
             true
         }
         R.id.terrain_map -> {
             mMap.mapType=GoogleMap.MAP_TYPE_TERRAIN
             true
         }
         else -> super.onOptionsItemSelected(item)
     }

 }
