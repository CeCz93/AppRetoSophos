package com.example.appretosophos.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.appretosophos.R
import com.example.appretosophos.databinding.FragmentSendDocumentBinding
import com.example.appretosophos.model.DocumentsViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


class MapsFragment : Fragment() {

    //ViewModel for this fragment
    private val sharedViewModel: DocumentsViewModel by activityViewModels()

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val callback = OnMapReadyCallback { googleMap ->

        sharedViewModel.getOffices(googleMap)

        // Location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Check location permission
        checkLocationPermission()

        // Test Place
        val place = LatLng(6.2476, -75.5658)
        googleMap.addMarker(MarkerOptions().position(place).title("Ubicación actual"))

        // Set map to location
        googleMap.setMinZoomPreference(13.0f)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(place))
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101
                )
            }
        }
        return
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Maps", "Maps onCreateView")
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Maps", "Maps onViewCreated")

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }


}