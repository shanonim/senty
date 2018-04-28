package com.shanonim.senty.fragment

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.shanonim.senty.R
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.support.v4.view.animation.LinearOutSlowInInterpolator



/**
 * Fragment for Google Map
 */
@RuntimePermissions
class MapFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private val MIN_TIME: Long = 1000
    private val MIN_DISTANCE: Float = 50f

    private val mapFragment = SupportMapFragment()

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let {
                mMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // no-op
        }

        override fun onProviderEnabled(provider: String?) {
            // no-op
        }

        override fun onProviderDisabled(provider: String?) {
            // no-op
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        MapFragmentPermissionsDispatcher.setMapWithCheck(this)

        val fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { Toast.makeText(context, "tapped fab!", Toast.LENGTH_SHORT).show() }
        fab.visibility = View.GONE
        fab.viewTreeObserver
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        show(fab)
                    }
                })

        return view
    }

    interface InternalVisibilityChangedListener {
        fun onShown()
    }

    private val SHOW_HIDE_ANIM_DURATION = 200
    private val LINEAR_OUT_SLOW_IN_INTERPOLATOR = LinearOutSlowInInterpolator()

    fun show(view: View, listener: InternalVisibilityChangedListener?) {
        if (view.visibility == View.VISIBLE) return

        view.animate().cancel()

        view.alpha = 0f
        view.scaleY = 0f
        view.scaleX = 0f

        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(SHOW_HIDE_ANIM_DURATION.toLong())
                .setInterpolator(LINEAR_OUT_SLOW_IN_INTERPOLATOR)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        view.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        listener?.onShown()
                        super.onAnimationEnd(animation)
                    }
                })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        MapFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        val sydney = LatLng(-34.0, 151.0)
//        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun setMap() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_map, mapFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        mapFragment.getMapAsync(this)

        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener)
        }
    }
}
