package com.itp.pdbuddy.data.model

data class Prescription(
    val solutionType: String = "",
    val amount: String = "",
    val therapySchedule: String = "",
    val numberOfCycles: String = "",
    val additionalInstructions: String = "",
    val dateTime: String = "",
    val source: String = "",
    val username: String = "",
)