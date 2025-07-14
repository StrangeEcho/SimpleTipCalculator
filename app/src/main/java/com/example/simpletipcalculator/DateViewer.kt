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
import com.example.simpletipcalculator.utils.objs.Tip

class DateViewer : AppCompatActivity() {

    private lateinit var date: String
    private lateinit var tip: Tip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_date_viewer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Now safe to use intent and context
        date = intent.getStringExtra("SELECTED_DATE")!!
        val db = DatabaseHelper(this)
        tip = db.fetchTipByDate(date)!!

        displayTipData(tip)

        findViewById<Button>(R.id.DVBackBtn).setOnClickListener {
            startActivity(Intent(this, Calendar::class.java))
        }

        findViewById<Button>(R.id.DVEditBtn).setOnClickListener {
            val intent = Intent(this, TipAddScreen::class.java)
            intent.putExtra("MODE", "edit")
            intent.putExtra("DATE", date)
            intent.putExtra("CASH", tip.cash.toString())
            intent.putExtra("GROSS", tip.gross.toString())
            intent.putExtra("NOTE", tip.notes)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh tip from DB in case it was edited
        val db = DatabaseHelper(this)
        tip = db.fetchTipByDate(date)!!
        displayTipData(tip)
    }

    private fun displayTipData(tip: Tip) {
        findViewById<TextView>(R.id.DVViewingDate).text = date
        findViewById<TextView>(R.id.DVCashAmount).text = "Cash Amount\n$%.2f".format(tip.cash)
        findViewById<TextView>(R.id.DVGrossTip).text = "Gross Tip\n$%.2f".format(tip.gross)
        findViewById<TextView>(R.id.DVNetTip).text = "Net Tip\n$%.2f".format(tip.net)
        findViewById<TextView>(R.id.DVFinalAmt).text = "Final Amount\n$%.2f".format(tip.net + tip.cash)
        findViewById<TextView>(R.id.DVNotes).text = tip.notes.ifBlank { "No notes for this day." }
    }
}
