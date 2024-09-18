package com.example.csun_markers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

// Main
class MainActivity : AppCompatActivity(), SpotListAdapter.ItemListener {

    private lateinit var navBar: BottomNavigationView
    private val mapActivityRequestCode = 1
    private val spotViewModel: SpotViewModel by viewModels {
        SpotViewModelFactory((application as SpotApplication).spotRepo)
    }

    // ----- Establish RecyclerView, SearchView, Refresh Button and Nav Bar Here -----
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val refreshButton = findViewById<ImageButton>(R.id.button_refresh)
        val searchView = findViewById<SearchView>(R.id.search_view)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = SpotListAdapter()
        navBar = findViewById(R.id.bottom_nav_bar)

        adapter.setListener(this) // Set Recycler View Item Listener.

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Handle Nav Bar Stuff.
        navBar.setSelectedItemId(R.id.item_spots)
        navBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.item_map -> {
                        val intent = Intent(this@MainActivity, MapsActivity::class.java)
                        startActivityForResult(intent, mapActivityRequestCode)
                }

                R.id.item_spots -> null
            }
            true
        }

        // Handle Search View Queries.
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        // Handle Refresh Clicks.
        refreshButton.setOnClickListener {
            finish()
            startActivity(intent)
        }

        // Observer will update RecyclerView when changes are noticed.
        spotViewModel.allSpots.observe(this) {
            spots ->
                spots.let { adapter.submitList(it) }
        }

    }


    // ----- Handle passing and receiving data between Main and Maps Activity using result codes -----
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        navBar.setSelectedItemId(R.id.item_spots) // Update the nav bar!

        // Retrieve Bundle Information, consisting of two strings and an integer (tag data).
        if (requestCode == mapActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.extras?.let { bundle ->

                val title = bundle.getString("title")
                val desc = bundle.getString("desc")
                val image = bundle.getInt("image")

                if (title != null && desc != null) {
                    val spot = Spot(title, desc, image)
                    spotViewModel.insert(spot)
                }
                else {
                    Log.d("MainActivity!!!!", "Couldn't retrieve Marker Information")
                }
            }
        }
        else {
            print("Nope.")
        }
    }

    // ----- Display a Pop Up Window -----
    override fun onSpotClick(spot: Spot, position: Int) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popUpView: View = inflater.inflate(R.layout.custom_pop_up_window, null)

        val popUpTV = popUpView.findViewById<TextView>(R.id.tv_pop_up_title)
        val popUpImage = popUpView.findViewById<ImageView>(R.id.iv_pop_up_image)
        val popUpDesc = popUpView.findViewById<TextView>(R.id.tv_pop_up_desc)

        popUpTV.text = spot.title
        popUpImage.setImageResource(spot.imgView)
        popUpDesc.text = spot.description

        val popUpWindow = PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT, true)

        popUpView.setOnClickListener {
            popUpWindow.dismiss() // Dismiss PopUp if you tap the window itself.
        }

        popUpWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0)
    }

    // ----- Handle Item Delete Clicks -----
    override fun onDeleteClick(spot: Spot, position: Int) {
         spotViewModel.delete(spot)
    }

    // ----- Handle Item Go Clicks -----
    override fun onGoClick(spot: Spot) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            val uri = String.format(Locale.ENGLISH, "geo:0,0?q=csun+${spot.title}")
            data = Uri.parse(uri) // Pass in the selected spot's title to use as the search criteria.
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent) // Open up Google Maps App!
        }
    }

}