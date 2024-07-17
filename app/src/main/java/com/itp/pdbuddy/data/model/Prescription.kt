package com.itp.pdbuddy.data.model

data class Solution(
    val type: String = "",
    val bagVolume: String = ""
)

data class Prescription(
    val solutions: List<Solution> = listOf(),  // Ordered list of solutions
    val fillVolume: String = "",               // Fill volume in liters
    val numberOfCycles: String = "",           // Number of cycles
    val totalCycles: String = "",              // Total cycles
    val totalVolume: String = "",              // Total volume in liters
    val lastFill: String = "",                 // Last fill volume
    val capUp: Boolean = false,                // Indicates if cap up is required
    val additionalInstructions: String = "",   // Additional instructions
    val dateTime: String = "",                 // Date and time of prescription
    val source: String = "",                   // Source of prescription
    val username: String = ""                  // Username associated with the prescription
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "solutions" to solutions.mapIndexed { index, solution ->
                "Solution ${index + 1}" to solution.toMap()
            }.toMap(), // Converting list to ordered map
            "fillVolume" to fillVolume,
            "numberOfCycles" to numberOfCycles,
            "totalCycles" to totalCycles,
            "totalVolume" to totalVolume,
            "lastFill" to if (capUp) "None" else lastFill,
            "capUp" to capUp,
            "additionalInstructions" to additionalInstructions,
            "dateTime" to dateTime,
            "source" to source,
            "username" to username
        )
    }
}

fun Solution.toMap(): Map<String, String> {
    return mapOf(
        "type" to type,
        "bagVolume" to bagVolume
    )
}
