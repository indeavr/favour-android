package com.example.allfavour.data.model

import com.allfavour.graphql.api.MyOfferingsQuery
import com.allfavour.graphql.api.type.PeriodInput
import com.google.android.libraries.places.api.model.Period
import com.google.gson.internal.bind.util.ISO8601Utils
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException


data class PeriodModel(
    var startTime: Calendar,
    var endTime: Calendar,

    var startDate: Long,
    var endDate: Long?,

    var startHour: Int,
    var endHour: Int?
) {

    fun toInputType(): PeriodInput {

        return PeriodInput(
            startTime = this.startTime,
            endTime = this.endTime
        )
    }


    fun fromDate(date: Date): Date {
        val formatted = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .format(date)
        val str = formatted.substring(0, 22) + ":" + formatted.substring(22)

        return toCalendar(str)
    }

    fun toIso(date: Date): Date {
        val ISO8601 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
        return Date(ISO8601.format(date))
    }

    fun toCalendar(iso8601string: String): Date {
        val calendar = GregorianCalendar.getInstance()
        var s = iso8601string.replace("Z", "+00:00")
        try {
            s = s.substring(0, 22) + s.substring(23)  // to get rid of the ":"
        } catch (e: IndexOutOfBoundsException) {
            throw ParseException("Invalid length", 0)
        }

        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s)
        return date
    }


    companion object {
        fun fromGraphType(graphType: MyOfferingsQuery.Time): PeriodModel {
            return PeriodModel(
                startTime = graphType.startTime,
                endTime = graphType.endTime,
                startDate = graphType.startTime.get(Calendar.DATE).toLong(),
                endDate = graphType.endTime.get(Calendar.DATE).toLong(),
                startHour = graphType.startTime.get(Calendar.HOUR),
                endHour = graphType.endTime.get(Calendar.HOUR)
            )
        }
    }
}
