package com.example.homework1.logic

class GameManager(private val lifeCount: Int = 3) {

    val rows = 5
    val cols = 3

    val grid = Array(rows) { IntArray(cols) }

    var playerIndex = 1
        private set

    var crashes = 0
        private set

    val isGameOver: Boolean
        get() = crashes >= lifeCount

    fun movePlayerRight() {
        if (playerIndex < cols - 1) {
            playerIndex++
        }
    }

    fun movePlayerLeft() {
        if (playerIndex > 0) {
            playerIndex--
        }
    }

    fun getItemAt(row: Int, col: Int): Int {
        return grid[row][col]
    }

    fun updateGameStep() {
        // 1. בדיקת התנגשות: אנחנו בודקים את מצב הלוח לפני הזזת המכשולים.
        // זה מאפשר למכשול להגיע לשורה 4 ולהיעלם מיד לאחר הפגיעה.
        checkCollision()

        // 2. הזזה: Move all rows down (מ-4 ל-1)
        for (i in rows - 1 downTo 1) {
            for (j in 0 until cols) {
                grid[i][j] = grid[i - 1][j]
            }
        }

        // 3. יצירה: Generate new obstacle at top row (Row 0)
        // מנקים את השורה העליונה
        for (j in 0 until cols) {
            grid[0][j] = 0
        }

        // הגרלת מכשול
        val randomCol = (0 until cols).random()
        if ((0..1).random() == 1) {
            grid[0][randomCol] = 1
        }
    }

    private fun checkCollision() {
        val collisionRow = rows - 1 // שורה 4

        // האם בתא הפגיעה הפוטנציאלי (שורה 4) יש מכשול?
        if (grid[collisionRow][playerIndex] == 1) {
            crashes++
            // מנקים את התא מיד כדי שלא ייספר פעמיים בהזזה הבאה.
            grid[collisionRow][playerIndex] = 0
        }
    }
}