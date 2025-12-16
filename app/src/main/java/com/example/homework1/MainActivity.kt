package com.example.homework1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.homework1.logic.GameManager
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
    private val DELAY_MS: Long = 1000
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_helmet0),
            findViewById(R.id.main_IMG_helmet1),
            findViewById(R.id.main_IMG_helmet2)
        )

        main_IMG_bikes = arrayOf(
            findViewById(R.id.main_IMG_bike0),
            findViewById(R.id.main_IMG_bike1),
            findViewById(R.id.main_IMG_bike2)
        )

        // חיבור המטריצה
        main_IMG_puddles = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_puddle_00), findViewById(R.id.main_IMG_puddle_01), findViewById(R.id.main_IMG_puddle_02)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_puddle_10), findViewById(R.id.main_IMG_puddle_11), findViewById(R.id.main_IMG_puddle_12)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_puddle_20), findViewById(R.id.main_IMG_puddle_21), findViewById(R.id.main_IMG_puddle_22)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_puddle_30), findViewById(R.id.main_IMG_puddle_31), findViewById(R.id.main_IMG_puddle_32)
            ),
            arrayOf(
                findViewById(R.id.main_IMG_puddle_40), findViewById(R.id.main_IMG_puddle_41), findViewById(R.id.main_IMG_puddle_42)
            )
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
        if (!isTimerRunning) {
            isTimerRunning = true
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    timerTick()
                }
            }, 0, DELAY_MS)
        }
    }

    private fun stopTimer() {
        timer?.cancel()
        isTimerRunning = false
        timer = null
    }

    private fun timerTick() {
        gameManager.updateGameStep()

        runOnUiThread {
            refreshUI()
            checkGameOver()
        }
    }

    private fun refreshUI() {
        // 1. עדכון לוח המשחק (המטריצה)
        for (i in 0 until gameManager.rows) {
            for (j in 0 until gameManager.cols) {
                val value = gameManager.getItemAt(i, j)

                main_IMG_puddles[i][j].visibility =
                    if (value == 1) View.VISIBLE else View.INVISIBLE
            }
        }

        // 2. עדכון מיקום השחקן
        val currentPlayerIndex = gameManager.playerIndex
        for (i in 0 until 3) {
            main_IMG_bikes[i].visibility =
                if (i == currentPlayerIndex) View.VISIBLE else View.INVISIBLE
        }

        // 3. עדכון חיים (קסדות) - התיקון: משמאל לימין!
        val crashes = gameManager.crashes

        // עוברים על כל הלבבות
        for (i in 0 until main_IMG_hearts.size) {

            // אם האינדקס הנוכחי (i) קטן ממספר ההתרסקויות, אנחנו מסתירים אותו.
            // דוגמה: crashes=1. אינדקס 0 (השמאלי) יהיה מוסתר.
            if (i < crashes) {
                main_IMG_hearts[i].visibility = View.INVISIBLE
            } else {
                main_IMG_hearts[i].visibility = View.VISIBLE
            }
        }
    }

    private fun checkGameOver() {
        if (gameManager.isGameOver) {
            stopTimer()
            Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG).show()
        }
    }
}