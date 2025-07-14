package com.example.simpletipcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simpletipcalculator.utils.DatabaseHelper
import com.example.simpletipcalculator.utils.objs.Settings

class SettingsEdit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_edit) // Match your actual XML filename


        val nameInput = findViewById<EditText>(R.id.SEName)
        val emailInput = findViewById<EditText>(R.id.SEEmail)
        val phoneInput = findViewById<EditText>(R.id.SEPhone)
        val workplaceInput = findViewById<EditText>(R.id.SEWorkplace)
        val tipOutInput = findViewById<EditText>(R.id.SETipout)
        val saveBtn = findViewById<Button>(R.id.SESave)
        val cancelBtn = findViewById<Button>(R.id.SEBack)

        val dbHelper = DatabaseHelper(this)
        val currentSettings = dbHelper.getSettings()

        nameInput.setText(currentSettings.name)
        emailInput.setText(currentSettings.email)
        phoneInput.setText(currentSettings.phone)
        workplaceInput.setText(currentSettings.workplace)
        tipOutInput.setText(currentSettings.tipout.toString())

        saveBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val workplace = workplaceInput.text.toString().trim()
            val tipOut = tipOutInput.text.toString().toDoubleOrNull()

            if (name.isBlank() || email.isBlank() || phone.isBlank() || workplace.isBlank() || tipOut == null) {
                Toast.makeText(this, "Please complete all fields correctly.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val updatedSettings = Settings(name, email, phone, workplace, tipOut)
            dbHelper.updateSettings(updatedSettings)
            Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show()
            finish()
        }


        cancelBtn.setOnClickListener {
            finish()
        }
    }
}
