package com.example.practical7

import java.io.Serializable

data class Person(
    var id: String = "",
    var name: String = "",
    var emailId: String = "",
    var phoneNo: String = "",
    var address: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : Serializable
