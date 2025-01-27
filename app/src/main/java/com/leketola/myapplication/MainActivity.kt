package com.leketola.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Path
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var count = 0

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        val reset = findViewById<Button>(R.id.buttonReset)
        val imageView = findViewById<ImageView>(R.id.imageView)

        button.setOnClickListener {
            count++
            textView.text = "Clicked $count times"
            val newImageView = ImageView(this).apply {
                id = View.generateViewId()
                layoutParams = ConstraintLayout.LayoutParams(150, 150)
                setImageResource(R.drawable.cookie)
                visibility = View.VISIBLE
            }

            mainLayout.addView(newImageView)

            val constraintSet = ConstraintSet()
            constraintSet.clone(mainLayout)

            constraintSet.connect(
                newImageView.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            constraintSet.connect(
                newImageView.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
            )
            constraintSet.connect(
                newImageView.id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
            )

            constraintSet.applyTo(mainLayout)

            newImageView.post {
                val screenHeight = mainLayout.height
                val screenWidth = mainLayout.width

                val randomHeight =
                    (screenHeight * 0.2).toFloat() + (Math.random() * (screenHeight * 0.5)).toFloat()

                val randomDirection = if (Math.random() > 0.5) 1 else -1

                val controlX =
                    screenWidth / 2 + randomDirection * (150 + (Math.random() * 300).toFloat())
                val controlY = randomHeight

                val endX =
                    screenWidth / 2 + randomDirection * (250 + (Math.random() * 350).toFloat())
                val endY = randomHeight

                val arcPath = Path().apply {
                    moveTo(newImageView.x, newImageView.y)
                    quadTo(controlX, controlY, endX, endY)
                }

                val arcAnimator =
                    ObjectAnimator.ofFloat(newImageView, View.X, View.Y, arcPath).apply {
                        duration = 1500
                        interpolator = AccelerateInterpolator()
                    }

                val fallControlX =
                    screenWidth / 2 + randomDirection * (100 + Math.random() * 200).toFloat()
                val fallControlY = screenHeight * 0.6f
                val fallEndX =
                    screenWidth / 2 + randomDirection * (150 + Math.random() * 300).toFloat()
                val fallEndY = screenHeight + newImageView.height

                val fallPath = Path().apply {
                    moveTo(endX, endY)
                    quadTo(fallControlX, fallControlY, fallEndX, fallEndY.toFloat())
                }


                val fallAnimator =
                    ObjectAnimator.ofFloat(newImageView, View.X, View.Y, fallPath).apply {
                        duration = 1500
                        interpolator = DecelerateInterpolator()
                    }

                AnimatorSet().apply {
                    playSequentially(arcAnimator, fallAnimator)
                    start()
                }
            }
        }

        reset.setOnClickListener {
            count = 0
            textView.text = "Reset"

            for (i in mainLayout.childCount - 1 downTo 0) {
                val view = mainLayout.getChildAt(i)
                if (view is ImageView && view.id != R.id.textView && view.id != R.id.button && view.id != R.id.buttonReset) {
                    mainLayout.removeView(view)
                }
            }
        }
    }
}