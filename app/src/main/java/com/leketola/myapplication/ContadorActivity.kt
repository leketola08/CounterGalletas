package com.leketola.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ContadorActivity : AppCompatActivity() {
    private var count = 0
    private lateinit var textCont: TextView
    private val tagLocation = "ContadorActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textCont = findViewById(R.id.contador2)
        val extras = intent.extras
        if (extras != null) {
            count = extras.getInt("counterValue", 0)
            updateText()
        }
        val buttonVolver = findViewById<Button>(R.id.buttonVolver)
        buttonVolver.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateText() {
        textCont.text = "Galletas contadas: $count"
    }

    override fun onStop() {
        super.onStop()
        Log.d(tagLocation, "Método onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tagLocation, "Método onDestroy()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("valor", count)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        count = savedInstanceState.getInt("valor", count)
        updateText()
    }
}