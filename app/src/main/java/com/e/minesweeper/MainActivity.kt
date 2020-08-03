package com.e.minesweeper

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.e.minesweeper.model.game_board
import com.e.minesweeper.ui.minesweeper_view
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TryBut.setOnClickListener {
            msBoard.TryMode()
        }

        FlagBut.setOnClickListener {
            //msBoard.restart()
            msBoard.FlagMode()
        }

        btnRestart.setOnClickListener {
            resetAnim()
            msBoard.restart()
        }
    }

    public fun minesweeper_view.restart() {
        game_board.resetModel()
        invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun resetAnim() {
        val x = msBoard.getRight()
        val y = msBoard.getBottom()

        val startRadius = 0
        val endRadius = Math.hypot(msBoard.getWidth().toDouble(),
            msBoard.getHeight().toDouble()).toInt()

        val anim = ViewAnimationUtils.createCircularReveal(
            msBoard,
            x,
            y,
            startRadius.toFloat(),
            endRadius.toFloat()
        )

        msBoard.setVisibility(View.VISIBLE)
        anim.start()
    }
}
