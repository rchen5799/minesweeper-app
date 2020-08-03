package com.e.minesweeper.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.e.minesweeper.R
import com.e.minesweeper.MainActivity
import com.e.minesweeper.model.game_board
import com.google.android.material.snackbar.Snackbar
import java.lang.Boolean.TRUE

class minesweeper_view(contex: Context?, attrs: AttributeSet?) : View(contex, attrs) {

    var paintBackGround = Paint()
    var paintLine = Paint()
    var paintText = Paint()

    init {
        //Generate minefield
        game_board.generateMineField()
        paintBackGround.color = Color.WHITE
        paintBackGround.style = Paint.Style.FILL

        paintLine.color = Color.BLACK
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        paintText.textSize = height / 5f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRect(
            0f, 0f, width.toFloat(),
            height.toFloat(), paintBackGround
        )
        drawBoard(canvas)
    }

    private fun drawBoard(canvas: Canvas?) {
        // border
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        drawHorLines(canvas)
        drawVertLines(canvas)
        drawPlayers(canvas)
    }

    private fun drawVertLines(canvas: Canvas?) {
        for(i in 0..4) {
            canvas?.drawLine(
                (i*width / 5).toFloat(), 0f, (i*width / 5).toFloat(), height.toFloat(),
                paintLine
            )
        }
    }

    private fun drawHorLines(canvas: Canvas?) {
        for(i in 0..4) {
            canvas?.drawLine(
                0f, (i * height / 5).toFloat(), width.toFloat(),
                (i * height / 5).toFloat(), paintLine
            )
        }
    }

    //Drawing Player Settings
    private fun drawPlayers(canvas: Canvas?) {
        var MineGuess = Paint()
        MineGuess.setColor(Color.GREEN)

        for (i in 0..4) {
            for (j in 0..4) {
                if (game_board.getFieldContent(i, j) == game_board.TRY) {

                    var numMinesAround = game_board.checkMines(i ,j)
                    var minesAround = numMinesAround.toString()
                    canvas?.drawText(minesAround, (i* width / 5 + width/20).toFloat(), (j * height / 5 + height / 5).toFloat(), paintText)

                } else if (game_board.getFieldContent(i, j) == game_board.FLAG) {
                    val centerX = (i * width / 5 + width / 10).toFloat()
                    val centerY = (j * height / 5 + height / 10).toFloat()
                    val radius = height / 10 - 2

                    canvas?.drawCircle(centerX, centerY, radius.toFloat(), MineGuess)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            // 0,1; 0,2...
            val tX = event.x.toInt() / (width/5)
            val tY = event.y.toInt() / (height/5)

            if (tX < 5 && tY < 5 &&
                game_board.getFieldContent(tX, tY) == game_board.EMPTY) {

                game_board.setFieldContent(tX, tY)

                invalidate() // redraws the view, the onDraw(...) will be called

                game_board.checkGameOver()

                if((game_board.getGameOver() == TRUE) && (game_board.getLOSS() == TRUE)) {
                    //val mySnackbar = Snackbar.make(view, stringId, duration)
                    //mySnackbar.show()
                    Toast.makeText((context as MainActivity), context.getString(R.string.Loss), Toast.LENGTH_LONG).show()
                } else if((game_board.getGameOver() == TRUE) && (game_board.getWIN() == TRUE)) {
                    Toast.makeText((context as MainActivity), context.getString(R.string.Win), Toast.LENGTH_LONG).show()
                }
            }
        }

        return true
    }

    public fun FlagMode() {
        game_board.changeFlag()
    }

    public fun TryMode() {
        game_board.changeTry()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h
        setMeasuredDimension(d, d)
    }


}