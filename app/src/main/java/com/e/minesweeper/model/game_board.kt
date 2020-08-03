package com.e.minesweeper.model

import android.icu.text.UnicodeSet.EMPTY
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.util.*

object game_board {
    //0 = Safe, 1 = Bomb
    public val TRUE_VAL: Short = 0

    //TRUE: Try mode, FALSE: Flag mode
    public val Mode: Boolean = TRUE


    public val EMPTY: Short = -1
    public val TRY: Short = 0
    public val FLAG: Short = 1
    //tracking player input
    private var PLAYER_INPUT = TRY

    //A 5x5 board
    private val model = arrayOf(
        shortArrayOf(TRUE_VAL, TRUE_VAL, TRUE_VAL, TRUE_VAL, TRUE_VAL),
        shortArrayOf(TRUE_VAL, TRUE_VAL, TRUE_VAL, TRUE_VAL, TRUE_VAL),
        shortArrayOf(TRUE_VAL, TRUE_VAL, TRUE_VAL, TRUE_VAL, TRUE_VAL),
        shortArrayOf(TRUE_VAL, TRUE_VAL, TRUE_VAL, TRUE_VAL, TRUE_VAL),
        shortArrayOf(TRUE_VAL, TRUE_VAL, TRUE_VAL, TRUE_VAL, TRUE_VAL)
    )

    //A 5x5 board that keeps track of Player's moves
    private val playerModel = arrayOf(
        shortArrayOf(EMPTY,EMPTY,EMPTY,EMPTY,EMPTY),
        shortArrayOf(EMPTY,EMPTY,EMPTY,EMPTY,EMPTY),
        shortArrayOf(EMPTY,EMPTY,EMPTY,EMPTY,EMPTY),
        shortArrayOf(EMPTY,EMPTY,EMPTY,EMPTY,EMPTY),
        shortArrayOf(EMPTY,EMPTY,EMPTY,EMPTY,EMPTY)
    )

    //Receives the content on the game board
    fun getFieldContent(x: Int, y: Int) = playerModel[x][y]

    //Player click getting registered on the Gameboard
    fun setFieldContent(x: Int, y: Int) {
        playerModel[x][y] = PLAYER_INPUT
    }

    //getter function for inputVal
    fun getInputVal() = PLAYER_INPUT

    //Function to switch Flag/Try
    fun changeFlag() {
        PLAYER_INPUT = FLAG
    }

    fun changeTry() {
        PLAYER_INPUT = TRY
    }

    //Check GameOver
    private var GameOver: Boolean = FALSE
    private var LOSS: Boolean = FALSE
    private var WIN: Boolean = FALSE

    //getter function for GameOver
    fun getGameOver() = GameOver
    fun getLOSS() = LOSS
    fun getWIN() = WIN

    //Checking GameState
    fun checkGameOver() {
        LossCheck()
        WinCheck()
    }

    //Checks Win
    fun WinCheck() {
        var countMines = 0
        var counter = 0
        while(counter < 6) {
            if(playerModel[mineLoc[counter]][mineLoc[counter+1]] == FLAG) {
                countMines++
                counter = counter + 2
            } else {
                break
            }
        }
        if(countMines == 3) {
            WIN = TRUE
            GameOver = TRUE
        }
    }

    //Checks Loss
    fun LossCheck() {
        for(i in 0..4) {
            for(j in 0..4) {
                if((playerModel[i][j] != EMPTY) && (playerModel[i][j] != model[i][j])) {
                    GameOver = TRUE
                    LOSS = TRUE
                }
            }
        }
    }

    //Locations of randomly generated mines of the gameboard
    private var mineLoc = mutableListOf<Int>()

    //generate the minefield
    fun generateMineField() {
        generateMines()
        AddMines()
    }

    //Random 3 mines
    fun generateMines() {
        var tempLoc = mutableListOf<Int>()
        while(mineLoc.size < 6) {
            var randomValue = (0..4).random()
            tempLoc.add(randomValue)
            if(tempLoc.size == 2) {
                if (Collections.indexOfSubList(mineLoc, tempLoc) != -1) {
                    tempLoc.clear()
                } else {
                    for(i in tempLoc) {
                        mineLoc.add(i)
                    }
                    tempLoc.clear()
                }
            }
        }
    }

    //Adding mines to the gameboard
    fun AddMines() {
        var counter = 0
        while(counter < 6) {
            model[mineLoc[counter]][mineLoc[counter+1]] = FLAG
            counter = counter + 2
        }
    }

    //Check how many mines are around the clicked area
    fun checkMines(x: Int, y: Int): Int {
        var numMines: Int
        if(model[x][y] != FLAG) {
            numMines = checkCorner1(x,y)+checkCorner2(x,y)+checkCorner3(x,y)+checkCorner4(x,y)
            numMines = numMines+Side1(x,y)+Side2(x,y)+Side3(x,y)+Side4(x,y)
            numMines = numMines+checkMid(x,y)
        } else {
            numMines = -1
        }
        return numMines
    }

    fun checkCorner1(x: Int, y: Int): Int {
        var mineCount: Int = 0
        if((x==0) && (y==0)) {
            if(model[x+1][y] == FLAG) mineCount++
            if(model[x+1][y+1] == FLAG) mineCount++
            if(model[x][y+1] == FLAG) mineCount++
        }
        return mineCount
    }

    fun checkCorner2(x: Int, y: Int): Int {
        var mineCount: Int = 0
        if((x==4) && (y==0)) {
            if(model[x-1][y] == FLAG) mineCount++
            if(model[x-1][y+1] == FLAG) mineCount++
            if(model[x][y+1] == FLAG) mineCount++
        }
        return mineCount
    }

    fun checkCorner3(x: Int, y: Int): Int {
        var mineCount: Int = 0
        if((x==0) && (y==4)) {
            if(model[x+1][y] == FLAG) mineCount++
            if(model[x+1][y-1] == FLAG) mineCount++
            if(model[x][y-1] == FLAG) mineCount++
        }
        return mineCount
    }

    fun checkCorner4(x: Int, y: Int): Int {
        var mineCount: Int = 0
        if((x==4) && (y==4)) {
            if(model[x-1][y] == FLAG) mineCount++
            if(model[x-1][y-1] == FLAG) mineCount++
            if(model[x][y-1] == FLAG) mineCount++
        }
        return mineCount
    }

    fun Side1(x: Int, y: Int): Int {
        var mineCount: Int = 0
        if((x==0) && (y!=0) && (y!=4)) {
            if(model[x+1][y] == FLAG) mineCount++
            if(model[x+1][y-1] == FLAG) mineCount++
            if(model[x+1][y+1] == FLAG) mineCount++
            if(model[x][y+1] == FLAG) mineCount++
            if(model[x][y-1] == FLAG) mineCount++
        }
        return mineCount
    }

    fun Side2(x: Int, y: Int): Int {
        var mineCount: Int = 0
        if((x==4) && (y!=0) && (y!=4)) {
            if(model[x-1][y] == FLAG) mineCount++
            if(model[x-1][y-1] == FLAG) mineCount++
            if(model[x-1][y+1] == FLAG) mineCount++
            if(model[x][y+1] == FLAG) mineCount++
            if(model[x][y-1] == FLAG) mineCount++
        }
        return mineCount
    }

    fun Side3(x: Int, y: Int): Int {
        var mineCount: Int = 0
        if((x!=0) && (x!=4) && (y==4)) {
            if(model[x][y-1] == FLAG) mineCount++
            if(model[x-1][y] == FLAG) mineCount++
            if(model[x+1][y] == FLAG) mineCount++
            if(model[x+1][y-1] == FLAG) mineCount++
            if(model[x-1][y-1] == FLAG) mineCount++
        }
        return mineCount
    }

    fun Side4(x: Int, y: Int): Int {
        var mineCount: Int = 0
        if((x!=4) && (y==0) && (x!=0)) {
            if(model[x-1][y+1] == FLAG) mineCount++
            if(model[x+1][y+1] == FLAG) mineCount++
            if(model[x][y+1] == FLAG) mineCount++
            if(model[x-1][y] == FLAG) mineCount++
            if(model[x+1][y] == FLAG) mineCount++
        }
        return mineCount
    }

    fun checkMid(x: Int, y: Int): Int {
        var mineCount: Int = 0
        if((x!=4 && x!=0 && y!=4 && y!=0)) {
            if(model[x][y+1] == FLAG) mineCount++
            if(model[x][y-1] == FLAG) mineCount++
            if(model[x+1][y] == FLAG) mineCount++
            if(model[x-1][y] == FLAG) mineCount++
            if(model[x+1][y+1] == FLAG) mineCount++
            if(model[x-1][y+1] == FLAG) mineCount++
            if(model[x-1][y-1] == FLAG) mineCount++
            if(model[x+1][y-1] == FLAG) mineCount++
        }
        return mineCount
    }

    //Reset Function
    fun resetModel() {
        for (i in 0..4){
            for (j in 0..4){
                model[i][j] = TRUE_VAL
                playerModel[i][j] = EMPTY
            }
        }
        mineLoc.clear()
        generateMineField()
        LOSS = FALSE; GameOver = FALSE; WIN = FALSE
        PLAYER_INPUT = TRY
    }

}