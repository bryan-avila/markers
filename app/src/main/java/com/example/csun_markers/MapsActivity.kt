package com.example.csun_markers

import android.app.Activity
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.csun_markers.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    // ----- Establish Map Fragment & NavBar -----
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle Nav Bar stuff!
        val navBar = findViewById<BottomNavigationView>(R.id.bottom_nav_bar_maps)
        navBar.setSelectedItemId(R.id.item_map)
        navBar.setOnItemSelectedListener {
            when(it.itemId) {
                    R.id.item_map -> null
                    R.id.item_spots -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
            }
            true
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    // ----- Establish initial camera position, location markers and their data, and a SoundPool -----
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.setMinZoomPreference(15f) // Allow users to zoom out enough to capture all of CSUN.
        mMap.setInfoWindowAdapter(CustomInfoWindow(this)) // Customize Info Window Appearance.

        // Move camera upon start to csunLocation and set camera bounds.
        val csunLocation = LOCATION_CAMPUS
        mMap.moveCamera(CameraUpdateFactory.newLatLng(csunLocation)) // "Main" Focus!
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MAP_BOUNDS.center, 15.5F))
        mMap.setLatLngBoundsForCameraTarget(MAP_BOUNDS) // Prevent users from accessing the entire world map.

        addMarkers(mMap) // Add Markers to fill the map on start-up!

        // Build audio attributes to use for SoundPool, a less resource intensive way to play sound effects.
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        val soundID = soundPool.load(this, R.raw.tap, 1)

        // Markers will be added to the DB when long clicked!
        mMap.setOnInfoWindowLongClickListener { marker ->
            val title = marker.title
            val desc = marker.snippet
            val image = marker.tag as Int // Convert tag data to Integer!

            val bundle = Bundle().apply {
                putString("title", title)
                putString("desc", desc)
                putInt("image", image)
            }

            // Send data back to MainActivity as a bundle!
            val replyIntent = Intent()
            replyIntent.putExtras(bundle)
            setResult(Activity.RESULT_OK, replyIntent)

            soundPool.play(soundID, 0.3f, 0.3f, 0,0,1f) // Play a "Tap" Sound!

            finish()
        }
    }

    private fun addMarkers(mMap: GoogleMap) {

        // Add Marker and their respective data.
        mMap.addMarker(MarkerOptions().position(LOCATION_LIBRARY).title("CSUN Library").snippet("Study & Relax!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).contentDescription("Okay Boomer"))?.tag = R.drawable.library
        mMap.addMarker(MarkerOptions().position(LOCATION_JACARANDA).title("Jacaranda Hall").snippet("Nurses & Engineers!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))?.tag = R.drawable.jacaranda
        mMap.addMarker(MarkerOptions().position(LOCATION_CHAPARRAL).title("Chaparral Hall").snippet("Science & Math!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))?.tag = R.drawable.chaparral
        mMap.addMarker(MarkerOptions().position(LOCATION_SIERRA).title("Sierra Hall").snippet("English & History!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))?.tag = R.drawable.sierra
        mMap.addMarker(MarkerOptions().position(LOCATION_LIVE_OAK).title("Live Oak Hall").snippet("Math & Science!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))?.tag = R.drawable.liveoak
        mMap.addMarker(MarkerOptions().position(LOCATION_EUCALYPTUS).title("Eucalyptus Hall").snippet("More Math & Science!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))?.tag = R.drawable.eucalyptus
        mMap.addMarker(MarkerOptions().position(LOCATION_CAMPUS_COMPLEX).title("Store Complex").snippet("CSUN Store & More").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))?.tag = R.drawable.campuscomplex
        mMap.addMarker(MarkerOptions().position(LOCATION_SEQUOIA).title("Sequoia Hall").snippet("Health & Child Dev.!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))?.tag = R.drawable.sequoia
        mMap.addMarker(MarkerOptions().position(LOCATION_BOOKSTEIN).title("Bookstein Hall").snippet("Business & Economics!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))?.tag = R.drawable.bookstein
        mMap.addMarker(MarkerOptions().position(LOCATION_CYPRESS).title("Cypress Hall").snippet("Music & Rehearsals!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))?.tag = R.drawable.cypress
        mMap.addMarker(MarkerOptions().position(LOCATION_MANZANITA).title("Manzanita Hall").snippet("Arts, Media & Comms!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))?.tag = R.drawable.manzanita
        mMap.addMarker(MarkerOptions().position(LOCATION_REC_CENTER).title("Rec. Center").snippet("Gym & Rock Climbing!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))?.tag = R.drawable.reccenter
        mMap.addMarker(MarkerOptions().position(LOCATION_STUDENT_UNION).title("Student Union").snippet("Services & Food!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))?.tag = R.drawable.studunion
        mMap.addMarker(MarkerOptions().position(LOCATION_JEROME).title("Jerome Richfield \nHall").snippet("Ethnic/Gender Study!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)))?.tag = R.drawable.jerome
        mMap.addMarker(MarkerOptions().position(LOCATION_SORAYA).title("The Soraya").snippet("Performance Hall").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))?.tag = R.drawable.soraya
        mMap.addMarker(MarkerOptions().position(LOCATION_SAGEBRUSH).title("Sage Brush Hall").snippet("Comm. Studies!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))?.tag = R.drawable.sagebrush
        mMap.addMarker(MarkerOptions().position(LOCATION_BAYRAMIAN).title("Bayramian Hall").snippet("Admissions & Records!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))?.tag = R.drawable.bayramian
        mMap.addMarker(MarkerOptions().position(LOCATION_NORDHOFF).title("Nordhoff Hall").snippet("Theatre Department!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))?.tag = R.drawable.nordhoff

    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.spotlistsql.REPLY"
    }
}