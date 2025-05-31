package com.example.first_exercise

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.first_exercise.interfaces.HighScoreClickedCallback
import com.example.first_exercise.ui.RecordsFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ScoreActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var scores_FRAME_highscores: FrameLayout
    private lateinit var highScoresFragment: RecordsFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var toolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_score)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        initViews()
    }
    private fun findViews() {
        scores_FRAME_highscores = findViewById(R.id.scores_FRAME_highscores)
        toolBar = findViewById(R.id.scores_Toolbar)
    }

    private fun initViews() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.scores_fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        toolBar.setNavigationOnClickListener { view: View -> finish() }

        highScoresFragment = RecordsFragment()
        highScoresFragment.highScoreItemClicked =
            object : HighScoreClickedCallback {
                override fun highScoreItemClicked(lat: Double, lon: Double) {
                    zoomInMap(lat, lon)
                }
            }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.scores_FRAME_highscores, highScoresFragment)
            .commit()
    }
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    private fun zoomInMap(lat: Double, lon: Double) {
        val location = LatLng(lat, lon)
        googleMap.addMarker(MarkerOptions().position(location))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
    }

}