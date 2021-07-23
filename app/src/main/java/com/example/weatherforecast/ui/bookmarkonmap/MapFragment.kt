package com.example.weatherforecast.ui.bookmarkonmap

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.R
import com.example.weatherforecast.database.AppDatabase
import com.example.weatherforecast.database.entity.Bookmark
import com.example.weatherforecast.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    lateinit var binding: FragmentMapBinding
    private lateinit var map: GoogleMap
    private var latLong: LatLng? = LatLng(18.5204, 73.8567)
    private var marker: Marker? = null
    private lateinit var listener: MapListener
    private val markerOptions: MarkerOptions by lazy {
        MarkerOptions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addMap()
        binding.btnBookMark.setOnClickListener {
            if (::map.isInitialized) {
                val geocoder = Geocoder(activity, Locale.getDefault())
                val addresses: List<Address> =
                    latLong?.let {
                        geocoder.getFromLocation(
                            it.latitude,
                            it.longitude,
                            1
                        )
                    } as List<Address>

                if (addresses.isNotEmpty()) {
                    if (addresses[0].locality != null) {
                        activity?.let {
                            latLong?.let { latLong ->
                                showDialog(it, latLong, addresses[0].locality)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun showDialog(context: Context, latlng: LatLng, locality: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Do you want to bookmark ${locality}?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            activity?.lifecycleScope?.launch {
                val bookmark =
                    Bookmark(locality, latlng.latitude.toString(), latlng.longitude.toString())

                activity?.let {
                    val id = AppDatabase(it).getBookmarksDao().insert(bookmark)
                    bookmark.id = id.toInt()
                    if (::listener.isInitialized) {
                        listener.onLocationBookMarked(bookmark)
                    }
                    activity?.onBackPressed()
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            map.clear()
            dialog.dismiss()
        }

        builder.show()
    }

    private fun addMap() {
        val mapFragment = SupportMapFragment.newInstance()
        val fragmentTransaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.map, mapFragment)
        fragmentTransaction.commit()
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapClickListener(this)
        latLong?.let {
            addMarker(it)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 13f))
        }
    }

    override fun onMapClick(latlng: LatLng?) {
        addMarker(latlng)
        latLong = latlng
    }

    private fun addMarker(latlng: LatLng?) {
        marker?.remove()

        latlng?.let {
            markerOptions.position(it)
            marker = map.addMarker(markerOptions)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         * @return A new instance of fragment MapFragment.
         */
        @JvmStatic
        fun newInstance() = MapFragment()

        const val MAP_FRAGMENT = "mapFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as MapListener
        } catch (exception: Exception) {

        }
    }

    override fun onResume() {
        activity?.title = getString(R.string.select_loation)
        super.onResume()
    }

    interface MapListener {
        fun onLocationBookMarked(bookmark: Bookmark)
    }

}