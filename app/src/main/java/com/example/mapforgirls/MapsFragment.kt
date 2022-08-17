package com.example.mapforgirls

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
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
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.mapforgirls.data.entities.Scrap
import com.example.mapforgirls.data.local.ColumnDatabase
import com.example.mapforgirls.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_maps.*
import java.io.IOException
import java.util.*

class MapsFragment : Fragment() {
    lateinit var binding: FragmentMapsBinding
    var db = FirebaseFirestore.getInstance()
    var choicePharmacy: ArrayList<PharmacyData> = arrayListOf()
    var i = 0

    var locationManager : LocationManager? = null
    var latitude : Double = 0.0
    var longitude : Double = 0.0
    var REQUIRED_PERMISSIONS = arrayOf<String>( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val PERMISSIONS_REQUEST_CODE = 100
    lateinit var mainActivity: MainActivity

    companion object {
        private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private val markerIconSize = 100
    }

    private val callback = OnMapReadyCallback { googleMap ->

        db.collection("pharmacyInfo")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    i++
                    var location = LatLng(
                        document.data["latitude"].toString().toDouble(),
                        document.data["longitude"].toString().toDouble()
                    )

                    val markerIcon = BitmapUtils.resizeMapIcons(requireContext(), R.drawable.pharmacymarker, markerIconSize, markerIconSize)

                    val markerOptions = MarkerOptions()
                    markerOptions.title(document.data["pharmacyName"].toString())
                    markerOptions.position(location)
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerIcon))


                    val marker: Marker? = googleMap.addMarker(markerOptions)
                    marker?.tag =
                        document.data["pharmacyName"] as String + "/" + document.data["address"] as String + "/" + document.data["phoneNum"] as String + "/"

                    if (result.size() <= i)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
                }
            }
            .addOnFailureListener { e ->
                Log.w("Error", "Error getting documents", e)
            }

        // current marker
//        binding.mylocationButton.setOnClickListener {
//            var currentLocation : LatLng = getLocation()
//            if(currentLocation != LatLng(0.0, 0.0)) {       // 현재 위치 제대로 받아왔을 때
//                val markerOptions = MarkerOptions()
//                markerOptions.title("현재 위치")
//                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.resizeMapIcons(requireContext(), R.drawable.marker, markerIconSize, markerIconSize)))
//                googleMap.addMarker(markerOptions.position(currentLocation))
//
//                moveCamera(googleMap, currentLocation.latitude, currentLocation.longitude)
//
//                googleMap!!.setOnMarkerClickListener(null)
//            }
//        }

        cardView.visibility = View.GONE

        // 마커 클릭 시 카드뷰 띄움
        googleMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {
                cardView.visibility = View.VISIBLE

                // 약국별 상세정보
                cardView.visibility = View.VISIBLE
                var pharmacyName = binding.pharmacyTV
                var address = binding.addressTV
                var phoneNum = binding.phoneNumTV
                var arr = marker.tag.toString().split("/")
                pharmacyName.text = arr[0]
                address.text = arr [1]
                phoneNum.text = arr[2]

                return false
            }
        })

        // 맵 클릭 시 카드뷰 사라짐
        googleMap!!.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng) {
                cardView.visibility = View.GONE
            }
        })

        binding.searchButton.setOnClickListener {
            var text = binding.searchEdit.text.toString()
            search(text)
            moveCamera(googleMap, latitude, longitude)
        }
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

//        getLocation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


//    private fun getLocation() : LatLng {
//        locationManager = mainActivity.getSystemService(LOCATION_SERVICE) as LocationManager?
//        var userLocation: Location = getLatLng()
//        if(userLocation != null){
//            latitude = userLocation.latitude
//            longitude = userLocation.longitude
//            Log.d("CheckCurrentLocation", "현재 내 위치 값: ${latitude}, ${longitude}")
//
////            var mGeoCoder =  Geocoder(mainActivity.applicationContext, Locale.KOREAN)
////            var mResultList: List<Address>? = null
////            try{
////                mResultList = mGeoCoder.getFromLocation(
////                    latitude!!, longitude!!, 1
////                )
////            }catch(e: IOException){
////                e.printStackTrace()
////            }
////            if(mResultList != null){
////                Log.d("CheckCurrentLocation", mResultList[0].getAddressLine(0))
////
////            }
//        }
//        return LatLng(latitude, longitude)
//    }
//
//    private fun getLatLng(): Location {
//        var currentLatLng: Location? = null
//        var hasFineLocationPermission = ContextCompat.checkSelfPermission(mainActivity,
//            Manifest.permission.ACCESS_FINE_LOCATION)
//        var hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mainActivity,
//            Manifest.permission.ACCESS_COARSE_LOCATION)
//
//        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
//            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
//            currentLatLng = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//        }else{
//            if(ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, REQUIRED_PERMISSIONS[0])){
//                Toast.makeText(mainActivity, "앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
//                ActivityCompat.requestPermissions(mainActivity, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
//            }else{
//                ActivityCompat.requestPermissions(mainActivity, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
//            }
//            currentLatLng = getLatLng()
//        }
//        return currentLatLng!!
//    }

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


    // 지도 이동 애니메이션
    private fun moveCamera(map: GoogleMap?, latitude: Double, longitude: Double) {
        map?.let {
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 16f))
        }
    }

    private fun search(searchWord: String): LatLng {
        db?.collection("pharmacyInfo")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            choicePharmacy.clear()

            for (snapshot in querySnapshot!!.documents) {
                if (snapshot.getString("pharmacyName")!!.contains(searchWord)) {
                    var item = snapshot.toObject(PharmacyData::class.java)
                    choicePharmacy.add(item!!)

                    Log.d("Result: ", item.latitude.toString() + ", " + item.longitude.toString())

                    latitude = item.latitude
                    longitude = item.longitude
                }

            }

            if (choicePharmacy.isNullOrEmpty()) {
                Toast.makeText(activity, "해당 약국은 소녀돌봄약국이 아닙니다.", Toast.LENGTH_SHORT).show()
            }
        }

        return LatLng(latitude, longitude)
    }
}

object BitmapUtils {
    fun resizeMapIcons(context: Context, iconId: Int, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(context.getResources(), iconId)
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    /**
     * Demonstrates converting a [Drawable] to a [BitmapDescriptor],
     * for use as a marker icon.
     */
    private fun vectorToBitmap(context: Context, @DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor? {
        val vectorDrawable =
            ResourcesCompat.getDrawable(context.getResources(), id, null) ?: return null
        with(vectorDrawable) {
            val bitmap = Bitmap.createBitmap(
                intrinsicWidth,
                intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            setBounds(0, 0, canvas.width, canvas.height)
            DrawableCompat.setTint(this, color)
            draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

}