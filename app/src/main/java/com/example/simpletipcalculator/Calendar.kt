package com.example.simpletipcalculator

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Calendar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calendar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextDateTip = findViewById<EditText>(R.id.editTextTipDate)
        val backBtn = findViewById<Button>(R.id.btnBack)
        val viewBtn = findViewById<Button>(R.id.btnView)

        // Back Button → Return to MainActivity
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Date Picker on EditText Click
        editTextDateTip.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, yearSelected, monthSelected, daySelected ->
                val selectedDate = "${monthSelected + 1}/$daySelected/$yearSelected"
                editTextDateTip.setText(selectedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        // View Button → Send date to DateViewer activity
        viewBtn.setOnClickListener {
            val selectedDate = editTextDateTip.text.toString()

            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Please select a Date to view", Toast.LENGTH_LONG).show()
            }
            else {
                val intent = Intent(this, DateViewer::class.java)
                intent.putExtra("SELECTED_DATE", selectedDate)
                startActivity(intent)
            }
        }
    }
}
