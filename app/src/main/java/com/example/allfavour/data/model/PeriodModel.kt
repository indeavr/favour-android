package com.example.allfavour.data.model

import java.util.*

data class PeriodModel(
    var startDate: Date,
    var startHour: Date,
    var endDate: Date?,
    var endHour: Date?
) {

    fun toInputType() {

    }
}