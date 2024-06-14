package com.itp.pdbuddy.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
}