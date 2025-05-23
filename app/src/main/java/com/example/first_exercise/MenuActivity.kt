package com.example.first_exercise

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.first_exercise.utilities.Constants
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class MenuActivity : AppCompatActivity() {
    private lateinit var menu_SWITCH_buttons: SwitchMaterial
    private lateinit var menu_SWITCH_speed: SwitchMaterial
    private lateinit var menu_BTN_highScores: MaterialButton
    private lateinit var menu_BTN_play: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        initViews()
    }

    private fun findViews() {
        menu_BTN_play = findViewById(R.id.menu_BTN_play)
        menu_SWITCH_buttons = findViewById(R.id.menu_SWITCH_buttons)
        menu_SWITCH_speed = findViewById(R.id.menu_SWITCH_speed)
        menu_BTN_highScores = findViewById(R.id.menu_BTN_highScores)
    }
    private fun initViews() {
        menu_BTN_play.setOnClickListener {view: View -> changeToMainActivity()}
        menu_BTN_highScores.setOnClickListener { view: View -> openScoresFragment() }
    }

    private fun changeToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        val isButtonsOn = menu_SWITCH_buttons.isChecked
        val isFastOn = menu_SWITCH_speed.isChecked
        var bundle = Bundle()
        bundle.putBoolean(Constants.BundleKeys.BUTTONS_MODE, isButtonsOn)
        bundle.putBoolean(Constants.BundleKeys.FAST_MODE, isFastOn)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun openScoresFragment() {
        val intent = Intent(this, ScoreActivity::class.java)
        startActivity(intent)
    }

}