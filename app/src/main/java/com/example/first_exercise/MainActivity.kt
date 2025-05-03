package com.example.first_exercise

import android.os.Bundle
import android.util.Log
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
import com.example.first_exercise.logic.GameManager
import com.example.first_exercise.utilities.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_cells: List<List<AppCompatImageView>>

    private lateinit var main_IMG_hearts: List<AppCompatImageView>

    private lateinit var main_BTN_left: MaterialButton

    private lateinit var main_BTN_right: MaterialButton

    private lateinit var gameManager: GameManager


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
        gameManager = GameManager(main_IMG_hearts.size)
        setupButtons()
        startSchedueler()
    }


    private fun findViews() {
        main_IMG_cells = List(Constants.GameDetails.ROWS) { row ->
            List(Constants.GameDetails.COLS) { col ->
                val id = resources.getIdentifier("cell_${row}_${col}", "id", packageName)
                findViewById(id)
            }
        }

        main_IMG_hearts = listOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
        main_BTN_right = findViewById(R.id.main_FAB_Right)
        main_BTN_left = findViewById(R.id.main_FAB_Left)
    }

    private fun setupButtons() {
        main_BTN_left.setOnClickListener {view: View -> moveLeft()}
        main_BTN_right.setOnClickListener {view: View -> moveRight()}
    }

    private fun moveLeft() {
        gameManager.moveLeft()
        updateCarUI()
    }

    private fun moveRight() {
        gameManager.moveRight()
        updateCarUI()
    }


    private fun updateUI() {
        if (gameManager.isGameOver) {
            Log.d("Game Status", "Game over, starting new one")
            gameManager.startNewGame()
            fillHearts()
        }
        for (i in 0 until Constants.GameDetails.ROWS) {
            for (j in 0 until Constants.GameDetails.COLS) {
                if (i != Constants.GameDetails.ROWS - 1 || j != gameManager.carLane){
                    val cell = main_IMG_cells[i][j]
                    val isObstacle = gameManager.objectsMatrix[i][j] == 1


                    cell.setImageResource(
                        when {
                            isObstacle -> R.drawable.glacier
                            else -> android.R.color.transparent
                        }
                    )
                }
            }
        }

        if (gameManager.crashes != 0){
            main_IMG_hearts[main_IMG_hearts.size - gameManager.crashes]
                .visibility = View.INVISIBLE
        }
    }

    private fun updateCarUI() {
        for (lane in 0 until Constants.GameDetails.COLS)
        {
            val cell = main_IMG_cells[main_IMG_cells.lastIndex][lane]
            if (lane == gameManager.carLane) {
                cell.setImageResource(R.drawable.penguin)
            }
            else {
                cell.setImageResource(0)
            }
        }
    }

    private fun fillHearts() {
        for (i in main_IMG_hearts.indices) {
            main_IMG_hearts[i].visibility = View.VISIBLE
        }
    }

    private fun startSchedueler() {
        lifecycleScope.launch {
            while (true) {
                delay(Constants.scheduler.DELAY)
                gameManager.updateMatrix()
                updateUI()
                Log.d("Schedueler", "ran schedueler to update game")
            }
        }
    }
}