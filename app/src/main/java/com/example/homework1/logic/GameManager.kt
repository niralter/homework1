package com.example.homework1.logic

import com.example.homework1.utilities.Constants

class GameManager(private val lifeCount: Int = Constants.Game.LIFE_COUNT) {
    val rows = Constants.Game.ROWS
    val cols = Constants.Game.COLS
    val grid = Array(rows) { IntArray(cols) }
    var playerIndex = 1
        private set
    var crashes = 0
        private set
    val isGameOver: Boolean
        get() = crashes >= lifeCount

    fun movePlayerRight() {
        if (playerIndex < cols - 1) playerIndex++
    }

    fun movePlayerLeft() {
        if (playerIndex > 0) playerIndex--
    }

    fun getItemAt(row: Int, col: Int): Int = grid[row][col]

    fun updateGameStep(): Boolean {
        val hit = checkCollision()

        for (i in rows - 1 downTo 1) {
            for (j in 0 until cols) {
                grid[i][j] = grid[i - 1][j]
            }
        }

        for (j in 0 until cols) grid[0][j] = 0
        val randomCol = (0 until cols).random()
        if ((0..1).random() == 1) grid[0][randomCol] = 1

        return hit
    }

    private fun checkCollision(): Boolean {
        val collisionRow = rows - 1
        if (grid[collisionRow][playerIndex] == 1) {
            crashes++
            grid[collisionRow][playerIndex] = 0
            return true
        }
        return false
    }
}