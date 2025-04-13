package com.example.first_exercise

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

    private val ROWS = 6
    private val COLS = 3
    private var carLane = 1
    private var lives = 3

    private lateinit var cellViews: List<List<AppCompatImageView>>
    private lateinit var heartViews: List<AppCompatImageView>
    private lateinit var main_BTN_left: MaterialButton
    private lateinit var main_BTN_right: MaterialButton

    private var grid = Array(ROWS) { IntArray(COLS) { 0 } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        setupButtons()

        lifecycleScope.launch {
            while (true) {
                delay(1500L)
                updateGame()
            }
        }

    }


    // Set up the GridLayout and ImageViews
    private fun findViews() {
        cellViews = List(ROWS) { row ->
            List(COLS) { col ->
                val id = resources.getIdentifier("cell_${row}_${col}", "id", packageName)
                findViewById(id)
            }
        }

        heartViews = listOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
        main_BTN_right = findViewById(R.id.main_FAB_Right)
        main_BTN_left = findViewById(R.id.main_FAB_Left)
    }

    private fun setupButtons() {
        main_BTN_left.setOnClickListener {
            if (carLane > 0) carLane--
            updateUI()
        }

        main_BTN_right.setOnClickListener {
            if (carLane < COLS - 1) carLane++
            updateUI()
        }
    }


    private fun updateGame() {
        // Move grid down
        for (i in ROWS - 1 downTo 1) {
            grid[i] = grid[i - 1].copyOf()
        }
        // Generate random obstacle on top row
        val generatePercent = (0 until 100).random()
        val isGenerateNew = if (generatePercent < 65) true else false
        if (isGenerateNew) {
            val obstacleLane = (0 until COLS).random()
            grid[0] = IntArray(COLS) { if (it == obstacleLane) 1 else 0 }
        }
        else {
            grid[0] = IntArray(COLS) {0}
        }

        // Collision check
        if (grid[ROWS - 1][carLane] == 1) {
            lives--
            if (lives <= 0) {
                lives = 3
                grid = Array(ROWS) { IntArray(COLS) { 0 } }
            }
        }

        updateUI()
    }

    private fun updateUI() {
        for (i in 0 until ROWS) {
            for (j in 0 until COLS) {
                val cell = cellViews[i][j]
                val isCar = (i == ROWS - 1 && j == carLane)
                val isObstacle = grid[i][j] == 1

                cell.setImageResource(
                    when {
                        isCar -> R.drawable.spaceship
                        isObstacle -> R.drawable.meteorite
                        else -> android.R.color.transparent
                    }
                )
            }
        }

        for (i in heartViews.indices) {
            heartViews[i].visibility = if (i < lives) View.VISIBLE else View.INVISIBLE
        }
    }
}