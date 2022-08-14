package com.example.mapforgirls

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.mapforgirls.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_maps.*
import java.io.IOException
import java.util.*

class MapsFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentMapsBinding

    var locationManager : LocationManager? = null
    var latitude : Double = 0.0
    var longitude : Double = 0.0
    var REQUIRED_PERMISSIONS = arrayOf<String>( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val PERMISSIONS_REQUEST_CODE = 100
    lateinit var mainActivity: MainActivity

    private val callback = OnMapReadyCallback { googleMap ->

        val point = LatLng(37.626326, 127.093241)
        googleMap.addMarker(MarkerOptions().position(point).title("서울여자대학교"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(point))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(13f))

        var currentLocation : LatLng = getLocation()
        if(currentLocation != LatLng(0.0, 0.0)) {       // 현재 위치 제대로 받아왔을 때
            googleMap.addMarker(MarkerOptions().position(currentLocation).title("현재 위치"))
        }

        cardView.visibility = View.GONE

        // 마커 클릭 시 카드뷰 띄움
        googleMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 액티비티로 형변환해서 할당
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)

        // 버큰 클릭 리스너
        binding.searchButton.setOnClickListener(this)
        binding.mylocationButton.setOnClickListener(this)

        getLocation()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.searchButton -> {
                // test 메시지
                Toast.makeText(activity, "검색완료", Toast.LENGTH_SHORT).show()

                // 버튼 클릭 시 지도 화면 변경
            }
            R.id.mylocationButton -> {

            }
        }

    }

    private fun getLocation() : LatLng {
        locationManager = mainActivity.getSystemService(LOCATION_SERVICE) as LocationManager?
        var userLocation: Location = getLatLng()
        if(userLocation != null){
            latitude = userLocation.latitude
            longitude = userLocation.longitude
            Log.d("CheckCurrentLocation", "현재 내 위치 값: ${latitude}, ${longitude}")

            var mGeoCoder =  Geocoder(mainActivity.applicationContext, Locale.KOREAN)
            var mResultList: List<Address>? = null
            try{
                mResultList = mGeoCoder.getFromLocation(
                    latitude!!, longitude!!, 1
                )
            }catch(e: IOException){
                e.printStackTrace()
            }
            if(mResultList != null){
                Log.d("CheckCurrentLocation", mResultList[0].getAddressLine(0))

            }
        }
        return LatLng(latitude, longitude)
    }

    private fun getLatLng(): Location {
        var currentLatLng: Location? = null
        var hasFineLocationPermission = ContextCompat.checkSelfPermission(mainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION)
        var hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mainActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
            currentLatLng = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, REQUIRED_PERMISSIONS[0])){
                Toast.makeText(mainActivity, "앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(mainActivity, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }else{
                ActivityCompat.requestPermissions(mainActivity, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }
            currentLatLng = getLatLng()
        }
        return currentLatLng!!
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {
            var check_result = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if (check_result) {
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        mainActivity,
                        REQUIRED_PERMISSIONS[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        mainActivity,
                        REQUIRED_PERMISSIONS[1]
                    )
                ) {
                    Toast.makeText(
                        mainActivity,
                        "권한 설정이 거부되었습니다.\n앱을 사용하시려면 다시 실행해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.remove(this)
                        ?.commit()
                } else {
                    Toast.makeText(mainActivity, "권한 설정이 거부되었습니다.\n설정에서 권한을 허용해야 합니다..", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}

