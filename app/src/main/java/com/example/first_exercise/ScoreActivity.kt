package com.example.first_exercise

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.first_exercise.ui.RecordsFragment

class ScoreActivity : AppCompatActivity() {

    private lateinit var scores_FRAME_highscores: FrameLayout
    private lateinit var highScoresFragment: RecordsFragment
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
//        mapFragment = MapFragment()
//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.main_FRAME_map, mapFragment)
//            .commit()
        toolBar.setNavigationOnClickListener { view: View -> finish() }

        highScoresFragment = RecordsFragment()
//        highScoresFragment.highScoreItemClicked =
//            object : Callback_HighScoreClicked {
//                override fun highScoreItemClicked(lat: Double, lon: Double) {
//                    mapFragment.zoom(lat, lon)
//                }
//            }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.scores_FRAME_highscores, highScoresFragment)
            .commit()
    }
}