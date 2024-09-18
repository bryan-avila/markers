package com.example.csun_markers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker


// Concrete implementation of InfoWindowAdapter Interface.
class CustomInfoWindow(context: Context) : InfoWindowAdapter {

    private val customInfoWindow: View = LayoutInflater.from(context).inflate(R.layout.custom_info_window_layout, null)

    private fun windowConfiguration(view: View, marker: Marker) {
        val textViewTitle = view.findViewById<TextView>(R.id.tv_info_window_title)
        val textViewSnippet = view.findViewById<TextView>(R.id.tv_info_window_snippet)
        val markerTitle = marker.title
        val markerSnippet = marker.snippet

        textViewTitle.text = markerTitle
        textViewSnippet.text = markerSnippet
    }

    // ----- API makes second call here, for only changing contents -----
    override fun getInfoContents(givenMarker: Marker): View {
        windowConfiguration(customInfoWindow, givenMarker)
        return customInfoWindow
    }

    // ----- API first calls getInfoWindow, changing even the window appearance -----
    override fun getInfoWindow(givenMarker: Marker): View {
        windowConfiguration(customInfoWindow, givenMarker)
        return customInfoWindow
    }
}