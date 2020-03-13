package com.example.allfavour.ui

import com.google.firebase.database.Exclude
import java.util.*

data class FriendlyMessage(
    var text: String? = null,
    var name: String? = null,
    var photoUrl: String? = null,
    var imageUrl: String? = null,
    val time: String? = Calendar.getInstance().timeInMillis.toString()
) {

    var id: String? = null

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "text" to text,
            "name" to name,
            "photoUrl" to photoUrl,
            "imageUrl" to imageUrl,
            "time" to time
        )
    }
}