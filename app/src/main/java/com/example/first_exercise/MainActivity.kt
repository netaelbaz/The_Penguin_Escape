package com.example.first_exercise

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.first_exercise.interfaces.GameEventCallback
import com.example.first_exercise.interfaces.TiltCallback
import com.example.first_exercise.logic.GameManager
import com.example.first_exercise.utilities.Constants
import com.example.first_exercise.utilities.SharedPreferencesManager
import com.example.first_exercise.utilities.SingleSoundPlayer
import com.example.first_exercise.utilities.TiltDetector
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Job
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_cells: List<List<AppCompatImageView>>

    private lateinit var main_IMG_hearts: List<AppCompatImageView>

    private lateinit var main_BTN_left: MaterialButton

    private lateinit var main_BTN_right: MaterialButton

    private lateinit var main_LBL_score: MaterialTextView

    private lateinit var main_LBL_distance: MaterialTextView

    private lateinit var main_BTN_back: AppCompatImageButton

    private var isButtonsModeOn: Boolean = true

    private var isFastModeOn: Boolean = true

    private lateinit var timerJob: Job

    private lateinit var gameManager: GameManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var tiltDetector: TiltDetector? = null

    private lateinit var soundPlayer: SingleSoundPlayer


    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                SharedPreferencesManager.getInstance().putBoolean(Constants.SharedPreferences.LOCATION_PERMISSION_KEY, true)
            } else {
                SharedPreferencesManager.getInstance().putBoolean(Constants.SharedPreferences.LOCATION_PERMISSION_KEY, false)
            }
        }

    override fun onStart() {
        super.onStart()
        Log.d("on start", "called")
        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//        checkLocationPermission()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()
        findViews()
        initViews()
        startSchedueler()
    }


    private fun findViews() {
        main_BTN_back = findViewById(R.id.main_BTN_back)
        main_LBL_score = findViewById(R.id.main_LBL_score)
        main_LBL_distance = findViewById(R.id.main_LBL_distance)
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

    private fun initViews() {
        soundPlayer = SingleSoundPlayer(this)
        gameManager = GameManager(main_IMG_hearts.size)
        gameManager.gameEventListener = object: GameEventCallback {
            override fun onCrash() {
                // move toast here
                soundPlayer.playSound(R.raw.crashsound)
            }

            override fun onCoinCollecting() {
                soundPlayer.playSound(R.raw.coincollectionsound)
            }
        }

        val bundle: Bundle? = intent.extras

        isButtonsModeOn = bundle?.getBoolean(Constants.BundleKeys.BUTTONS_MODE,true) ?: true
        isFastModeOn = bundle?.getBoolean(Constants.BundleKeys.FAST_MODE,true) ?: true

//        main_LBL_score.text = buildString{
//            append("Score: ")
//            append(gameManager.getCurrentScore())
//        }
//        main_LBL_distance.text = buildString{
//            append("Distance: ")
////            append(gameManager.getTopScore())
//        }
        main_BTN_back.setOnClickListener {
            timerJob.cancel()
            finish()
        }
        if (isButtonsModeOn) {
            initMovementButtons()
        } else {
            hideMovementButtons()
            initTiltDetector()
        }
    }

    override fun onResume() {
        super.onResume()
        tiltDetector?.start()
    }

    override fun onPause() {
        super.onPause()
        tiltDetector?.stop()
    }

    private fun initMovementButtons() {
        main_BTN_left.setOnClickListener {view: View -> moveLeft()}
        main_BTN_right.setOnClickListener {view: View -> moveRight()}
    }

    private fun hideMovementButtons() {
        main_BTN_left.visibility = View.INVISIBLE
        main_BTN_right.visibility = View.INVISIBLE
    }

    private fun checkLocationPermission() {
        SharedPreferencesManager.getInstance().putBoolean(Constants.SharedPreferences.LOCATION_PERMISSION_KEY, false)
        if (SharedPreferencesManager.getInstance().getBoolean(Constants.SharedPreferences.LOCATION_PERMISSION_KEY, false)) {
            Log.d("LocationPermission", "Permission already granted.")
        } else {
            Log.d("LocationPermission", "Requesting permission")
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private fun moveLeft() {
        gameManager.moveLeft()
        updateCarUI()
    }

    private fun moveRight() {
        gameManager.moveRight()
        updateCarUI()
    }

    @SuppressLint("MissingPermission")
    private fun saveLocationOnGameOver() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it or handle gracefully
//            requestLocationPermission() // your own function to ask for permission
            Log.d("location", "no premission")
            return
        }
        fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
            null)
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("location", "my location is ${latitude}, ${longitude}")
                    gameManager.gameOver(latitude, longitude)
                }
                else {
                    Log.d("location", "is null")
                }
            }
            .addOnFailureListener {
                Log.d("location", "failed")
            }
    }


    private fun updateUI() {
        if (gameManager.isGameOver) {
            Log.d("Game Status", "Game over")
            saveLocationOnGameOver()
            changeActivity()
//            gameManager.startNewGame()
//            fillHearts()
        }
        else {
            for (i in 0 until Constants.GameDetails.ROWS) {
                for (j in 0 until Constants.GameDetails.COLS) {
                    if (i != Constants.GameDetails.ROWS - 1 || j != gameManager.carLane){
                        val cell = main_IMG_cells[i][j]
                        val isObstacle = gameManager.objectsMatrix[i][j] == Constants.objectValues.obstacleValue
                        val isCoin = gameManager.objectsMatrix[i][j] == Constants.objectValues.coinValue


                        cell.setImageResource(
                            when {
                                isObstacle -> R.drawable.glacier
                                isCoin -> R.drawable.coin
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
            main_LBL_score.text = buildString{
                append("Score: ")
                append(gameManager.getCurrentScore())
            }
            main_LBL_distance.text = buildString{
                append("Distance: ")
                append(gameManager.distance)
            }
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

//    private fun fillHearts() {
//        for (i in main_IMG_hearts.indices) {
//            main_IMG_hearts[i].visibility = View.VISIBLE
//        }
//    }

    private fun startSchedueler() {
        timerJob = lifecycleScope.launch {
            while (true) {
                val delay = if (isFastModeOn) Constants.scheduler.FAST_DELAY else Constants.scheduler.SLOW_DELAY
                delay(delay)
                gameManager.updateMatrix()
                updateUI()
                Log.d("Schedueler", "ran schedueler to update game")
            }
        }
    }

    private fun changeActivity() {
        val intent = Intent(this, GameOverActivity::class.java)
        var bundle = Bundle()
        bundle.putInt(Constants.BundleKeys.SCORE_KEY, gameManager.getCurrentScore())
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
        timerJob.cancel()
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            context = this,
            tiltCallback = object : TiltCallback{
                override fun tiltLeft() {
                    moveLeft()
                }

                override fun tiltRight() {
                    moveRight()
                }

            }
        )
    }
}