package com.example.first_exercise

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.first_exercise.utilities.Constants
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class GameOverActivity : AppCompatActivity() {

    private lateinit var gameOver_LBL_score :MaterialTextView

    private lateinit var gameOver_BTN_menu: MaterialButton
    private lateinit var gameOver_BTN_scores: MaterialButton
    private lateinit var gameOver_BTN_play: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_over)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        initViews()
    }

    private fun findViews() {
        gameOver_LBL_score = findViewById(R.id.gameOver_LBL_score)
        gameOver_BTN_menu = findViewById(R.id.gameOver_BTN_menu)
        gameOver_BTN_scores = findViewById(R.id.gameOver_BTN_scores)
        gameOver_BTN_play = findViewById(R.id.gameOver_BTN_play)
    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras

        val score = bundle?.getInt(Constants.BundleKeys.SCORE_KEY,0)

        gameOver_LBL_score.text = buildString {
            append("Score: ")
            append(score)
        }
        gameOver_BTN_menu.setOnClickListener { view: View -> changeActivity("menu") }
        gameOver_BTN_scores.setOnClickListener { view: View -> changeActivity("scores") }
        gameOver_BTN_play.setOnClickListener { view: View -> changeActivity("play") }
    }

    private fun changeActivity(destActivity: String) {
        val intent = when (destActivity) {
            "menu" -> Intent(this, MenuActivity::class.java)
            "play" -> Intent(this, MainActivity::class.java)
            else -> {
                return
            }
        }
        startActivity(intent)
        finish()
    }
}