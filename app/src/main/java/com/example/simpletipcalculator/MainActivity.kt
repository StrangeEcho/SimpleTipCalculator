package com.example.simpletipcalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val helloMsg = findViewById<TextView>(R.id.helloMsg)
        helloMsg.text = "Hello Ruben"

        val totalCash = findViewById<TextView>(R.id.totalCash)
        totalCash.text = "Total Cash\n$100"

        val grossTip = findViewById<TextView>(R.id.grossTip)
        grossTip.text = "Gross Tip\n$250"

        val netTip = findViewById<TextView>(R.id.netTip)
        netTip.text = "Net Tip\n$150"

        val totalIncome = findViewById<TextView>(R.id.totalIncome)
        totalIncome.text = "Total Income (2025)\n$250"

        val addBtn = findViewById<Button>(R.id.btnQuickAdd)
        addBtn.setOnClickListener {
            val intent = Intent(this, TipAddScreen::class.java)
            startActivity(intent)
        }
        val calendarBtn = findViewById<Button>(R.id.btnCalendar)
        calendarBtn.setOnClickListener{
            val intent = Intent(this, Calendar::class.java)
            startActivity(intent)
        }


    }
}