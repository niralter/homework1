package com.example.homework1

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import com.example.homework1.logic.GameManager
import com.example.homework1.utilities.Constants
import com.example.homework1.utilities.SignalManager
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_puddles: Array<Array<AppCompatImageView>>
    private lateinit var main_IMG_bikes: Array<AppCompatImageView>
    private lateinit var main_IMG_hearts: Array<AppCompatImageView>
    private lateinit var main_BTN_left: AppCompatImageButton
    private lateinit var main_BTN_right: AppCompatImageButton

    private lateinit var gameManager: GameManager
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        SignalManager.init(this) // אתחול הסינגלטון
        gameManager = GameManager()

        findViews()
        initViews()
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    override fun onStop() {
        super.onStop()
        stopTimer()
    }

    private fun findViews() {
        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_BTN_right = findViewById(R.id.main_BTN_right)
        main_IMG_hearts = arrayOf(findViewById(R.id.main_IMG_helmet0), findViewById(R.id.main_IMG_helmet1), findViewById(R.id.main_IMG_helmet2))
        main_IMG_bikes = arrayOf(findViewById(R.id.main_IMG_bike0), findViewById(R.id.main_IMG_bike1), findViewById(R.id.main_IMG_bike2))
        main_IMG_puddles = arrayOf(
            arrayOf(findViewById(R.id.main_IMG_puddle_00), findViewById(R.id.main_IMG_puddle_01), findViewById(R.id.main_IMG_puddle_02)),
            arrayOf(findViewById(R.id.main_IMG_puddle_10), findViewById(R.id.main_IMG_puddle_11), findViewById(R.id.main_IMG_puddle_12)),
            arrayOf(findViewById(R.id.main_IMG_puddle_20), findViewById(R.id.main_IMG_puddle_21), findViewById(R.id.main_IMG_puddle_22)),
            arrayOf(findViewById(R.id.main_IMG_puddle_30), findViewById(R.id.main_IMG_puddle_31), findViewById(R.id.main_IMG_puddle_32)),
            arrayOf(findViewById(R.id.main_IMG_puddle_40), findViewById(R.id.main_IMG_puddle_41), findViewById(R.id.main_IMG_puddle_42))
        )
    }

    private fun initViews() {
        main_BTN_right.setOnClickListener {
            gameManager.movePlayerRight()
            refreshUI()
        }
        main_BTN_left.setOnClickListener {
            gameManager.movePlayerLeft()
            refreshUI()
        }
        refreshUI()
    }

    private fun startTimer() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                timerTick()
            }
        }, 0, Constants.Timer.DELAY)
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    private fun timerTick() {
        val isHit = gameManager.updateGameStep()

        runOnUiThread {
            if (isHit) {
                // רטט ו-Toast בכל פגיעה לפי הנחיות המטלה
                SignalManager.getInstance().toast("Crash!")
                SignalManager.getInstance().vibrate(500)
            }
            refreshUI()
            checkGameOver()
        }
    }

    private fun refreshUI() {
        for (i in 0 until gameManager.rows) {
            for (j in 0 until gameManager.cols) {
                main_IMG_puddles[i][j].visibility = if (gameManager.getItemAt(i, j) == 1) View.VISIBLE else View.INVISIBLE
            }
        }
        for (i in 0 until 3) {
            main_IMG_bikes[i].visibility = if (i == gameManager.playerIndex) View.VISIBLE else View.INVISIBLE
            main_IMG_hearts[i].visibility = if (i < gameManager.crashes) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun checkGameOver() {
        if (gameManager.isGameOver) {
            stopTimer()
            main_BTN_left.isEnabled = false // נטרול כפתורים בסיום המשחק
            main_BTN_right.isEnabled = false
            SignalManager.getInstance().toast("Game Over!")
        }
    }
}