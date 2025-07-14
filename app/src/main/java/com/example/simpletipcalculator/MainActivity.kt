package com.example.simpletipcalculator

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simpletipcalculator.utils.DatabaseHelper

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


        findViewById<Button>(R.id.DashQuickAddBtn).setOnClickListener {
            startActivity(Intent(this, TipAddScreen::class.java))
        }

        findViewById<Button>(R.id.DashCalendarBtn).setOnClickListener {
            startActivity(Intent(this, Calendar::class.java))
        }

        findViewById<Button>(R.id.DashSettingsBtn).setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        updateDashboardMetrics()
    }

    override fun onResume() {
        super.onResume()
        updateDashboardMetrics()
    }

    private fun updateDashboardMetrics() {
        val dbHelper = DatabaseHelper(this)
        val settings = dbHelper.getSettings()

        findViewById<TextView>(R.id.DashHelloMsg).setText("Hello ${settings.name}")
        findViewById<TextView>(R.id.DashMetricsTitle).setText(settings.workplace)

        findViewById<TextView>(R.id.DashTotalCash).text =
            "Total Cash\n$%.2f".format(dbHelper.getTotalCash())

        findViewById<TextView>(R.id.DashGrossTip).text =
            "Gross Tip\n$%.2f".format(dbHelper.getTotalGross())

        findViewById<TextView>(R.id.DashNetTip).text =
            "Net Tip\n$%.2f".format(dbHelper.getTotalNet())

        findViewById<TextView>(R.id.DashTotalIncome).text =
            "Total Income (2025)\n$%.2f".format(dbHelper.getTotalIncome())
    }
}
