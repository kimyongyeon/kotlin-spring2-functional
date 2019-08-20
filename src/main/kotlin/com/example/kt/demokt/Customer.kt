package com.example.kt.demokt

data class Customer (var id: Int =0, val name: String = "", val telephone: Telephone? = null) {
    data class Telephone (var countryCode: String = "", var telephoneNubmer: String = "")
}
