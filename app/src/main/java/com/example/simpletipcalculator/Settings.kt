package com.example.simpletipcalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simpletipcalculator.utils.DatabaseHelper
import org.w3c.dom.Text

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        updateSettingsDisplay()

        val backBtn = findViewById<Button>(R.id.SetBackBtn)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val editBtn = findViewById<Button>(R.id.SetEdit)
        editBtn.setOnClickListener {
            intent = Intent(this, SettingsEdit::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateSettingsDisplay()
    }
    private fun updateSettingsDisplay() {
        val name = findViewById<TextView>(R.id.SetName)
        val email = findViewById<TextView>(R.id.SetEmail)
        val phone = findViewById<TextView>(R.id.SetPhone)
        val workplace = findViewById<TextView>(R.id.SetWorkplace)
        val tipout = findViewById<TextView>(R.id.SetTipOut)
        val settings = DatabaseHelper(this).getSettings()

        name.text = "Name\n${settings.name}"
        email.text = "Email\n${settings.email}"
        phone.text = "Phone\n${settings.phone}"
        workplace.text = "Workplace\n${settings.workplace}"
        tipout.text = "Tipout\n${settings.tipout} (%${settings.tipout * 100})"
    }
}