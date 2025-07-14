package com.example.simpletipcalculator

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simpletipcalculator.utils.DatabaseHelper

class TipAddScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tip_add_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mode = intent.getStringExtra("MODE")
        val isEditMode = mode == "edit"

        val dateInput = findViewById<EditText>(R.id.TASDateSelector)
        val grossInput = findViewById<EditText>(R.id.TASGrossTip)
        val cashInput = findViewById<EditText>(R.id.TASCash)
        val notesInput = findViewById<EditText>(R.id.TASNotes)
        val saveBtn = findViewById<Button>(R.id.TASSaveButton)
        val backBtn = findViewById<Button>(R.id.TASBackBtn)

        val dbHelper = DatabaseHelper(this)

        if (isEditMode) {
            findViewById<TextView>(R.id.TASTitle).setText("Edit Tip Information")
            saveBtn.text = "Update Tip"
            dateInput.setText(intent.getStringExtra("DATE"))
            dateInput.isEnabled = false
            grossInput.setText(intent.getStringExtra("GROSS"))
            cashInput.setText(intent.getStringExtra("CASH"))
            notesInput.setText(intent.getStringExtra("NOTE"))
        }

        dateInput.setOnClickListener {
            val calendar = android.icu.util.Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val selectedDate = "${month + 1}/$dayOfMonth/$year"
                dateInput.setText(selectedDate)
            }, calendar.get(android.icu.util.Calendar.YEAR), calendar.get(android.icu.util.Calendar.MONTH), calendar.get(android.icu.util.Calendar.DAY_OF_MONTH))

            datePickerDialog.show()
        }

        backBtn.setOnClickListener {
            finish()
        }

        saveBtn.setOnClickListener {
            val date = dateInput.text.toString().trim()
            val gross = grossInput.text.toString().toDoubleOrNull()
            val cash = cashInput.text.toString().toDoubleOrNull()
            val notes = notesInput.text.toString()

            if (date.isBlank() || gross == null || cash == null) {
                Toast.makeText(this, "Please fill out date, gross, and cash fields.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!isEditMode && dbHelper.fetchTipByDate(date) != null) {
                Toast.makeText(this, "A tip for this date already exists. Please select a different date.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val tipOutPercent = dbHelper.getSettings().tipout
            val net = gross - (gross * tipOutPercent)

            val success = if (isEditMode) {
                dbHelper.updateTipByDate(date, gross, net, cash, notes)
            } else {
                dbHelper.insertTip(date, gross, net, cash, notes) != -1L
            }

            if (success) {
                Toast.makeText(this, if (isEditMode) "Tip updated!" else "Tip saved!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save tip.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
