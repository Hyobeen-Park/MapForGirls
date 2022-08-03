package com.example.mapforgirls

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        val point = LatLng(37.626326, 127.093241)
        googleMap.addMarker(MarkerOptions().position(point).title("서울여자대학교"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(point))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(13f))

        cardView.visibility = View.GONE

        // 마커 클릭 시 카드뷰 띄움
        googleMap!!.setOnMarkerClickListener(object: GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {
                cardView.visibility = View.VISIBLE

                // 약국별 모이는거 다르게 보이는거 추가할 예정

                return false
            }
        })

        // 맵 클릭 시 카드뷰 사라짐
        googleMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng) {
                cardView.visibility = View.GONE
            }
        })

        //getLocationPermission()
        //updateLocationUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


}