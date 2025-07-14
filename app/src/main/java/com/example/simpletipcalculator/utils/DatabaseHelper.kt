package com.example.simpletipcalculator.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import com.example.simpletipcalculator.utils.objs.Settings
import com.example.simpletipcalculator.utils.objs.Tip

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tips.db"
        private const val DATABASE_VERSION = 4

        // Tips table
        const val TABLE_TIPS = "tips"
        const val COLUMN_ID = "id"
        const val COLUMN_DATE = "date"
        const val COLUMN_GROSS = "gross"
        const val COLUMN_NET = "net"
        const val COLUMN_CASH = "cash"
        const val COLUMN_NOTES = "notes"

        // Settings table
        const val TABLE_SETTINGS = "settings"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_WORKPLACE = "workplace"
        const val COLUMN_TIPOUT = "tipout"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTipsTable = """
            CREATE TABLE $TABLE_TIPS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_GROSS REAL NOT NULL,
                $COLUMN_NET REAL NOT NULL,
                $COLUMN_CASH REAL NOT NULL,
                $COLUMN_NOTES TEXT
            )
        """.trimIndent()

        val createSettingsTable = """
            CREATE TABLE $TABLE_SETTINGS (
                $COLUMN_NAME TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_PHONE TEXT,
                $COLUMN_WORKPLACE TEXT,
                $COLUMN_TIPOUT REAL
            )
        """.trimIndent()

        db.execSQL(createTipsTable)
        db.execSQL(createSettingsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TIPS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SETTINGS")
        onCreate(db)
    }

    fun insertTip(date: String, gross: Double, net: Double, cash: Double, notes: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_DATE, date)
            put(COLUMN_GROSS, gross)
            put(COLUMN_NET, net)
            put(COLUMN_CASH, cash)
            put(COLUMN_NOTES, notes)
        }
        return writableDatabase.insert(TABLE_TIPS, null, values)
    }

    fun fetchTipByDate(date: String): Tip? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_TIPS WHERE $COLUMN_DATE = ? LIMIT 1",
            arrayOf(date)
        )

        val tip: Tip? = if (cursor.moveToFirst()) {
            Tip(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                gross = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GROSS)),
                net = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_NET)),
                cash = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_CASH)),
                notes = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES)) ?: ""
            )
        } else {
            null
        }

        cursor.close()
        return tip
    }

    fun updateTipByDate(date: String, gross: Double, net: Double, cash: Double, notes: String): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_GROSS, gross)
            put(COLUMN_NET, net)
            put(COLUMN_CASH, cash)
            put(COLUMN_NOTES, notes)
        }
        val result = writableDatabase.update(TABLE_TIPS, values, "$COLUMN_DATE = ?", arrayOf(date))
        return result > 0
    }

    fun getTotalCash(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_CASH) FROM $TABLE_TIPS", null)
        val total = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return total
    }

    fun getTotalGross(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_GROSS) FROM $TABLE_TIPS", null)
        val total = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return total
    }

    fun getTotalNet(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_NET) FROM $TABLE_TIPS", null)
        val total = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return total
    }

    fun getTotalIncome(): Double {
        return getTotalNet() + getTotalCash()
    }


    fun getSettings(): Settings {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_SETTINGS LIMIT 1", null)

        val settings: Settings = if (cursor.moveToFirst()) {
            Settings(
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                workplace = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORKPLACE)),
                tipout = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TIPOUT))
            )
        } else {
            Settings(
                name = "User",
                email = "N/A",
                phone = "N/A",
                workplace = "N/A",
                tipout = 0.00
            )
        }

        cursor.close()
        return settings
    }

    fun updateSettings(settings: Settings): Long {
        val db = writableDatabase

        db.delete(TABLE_SETTINGS, null, null) // single-row table

        val values = ContentValues().apply {
            put(COLUMN_NAME, settings.name)
            put(COLUMN_EMAIL, settings.email)
            put(COLUMN_PHONE, settings.phone)
            put(COLUMN_WORKPLACE, settings.workplace)
            put(COLUMN_TIPOUT, settings.tipout)
        }

        return db.insert(TABLE_SETTINGS, null, values)
    }






}
