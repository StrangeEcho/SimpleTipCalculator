package com.example.simpletipcalculator.utils.objs

data class Tip(
    val id: Int,
    val date: String,
    val gross: Double,
    val net: Double,
    val cash: Double,
    val notes: String
)
