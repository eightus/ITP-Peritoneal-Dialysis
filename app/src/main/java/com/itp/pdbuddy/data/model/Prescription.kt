package com.itp.pdbuddy.data.model

data class Solution(
    val type: String = "",
    val bagVolume: String = ""
)

data class Prescription(
    val solutions: List<Solution> = listOf(),
    val fillVolume: String = "",
    val numberOfCycles: String = "",
    val totalCycles: String = "",
    val totalVolume: String = "",
    val lastFill: String = "",
    val capUp: Boolean = false,
    val additionalInstructions: String = "",
    val dateTime: String = "",
    val source: String = "",
    val username: String = ""
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
