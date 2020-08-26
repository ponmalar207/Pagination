package com.example.kotlinpagination

data class User(
    var name: String,
    var profileImage: String,
    var ratings: Double,
    var totalRatings: Int,
    var specialist: String,
    var languages: String,
    var experience: String,
    var charge: String,
    var waitingTime: String
)
//data class User(
//    var name:String,
//    var profileImage: String,
//    var ratings:Double,
//    var totalRatings:Int,
//    var specialist:List<String>,
//    var languages:List<String>,
//    var experience:String,
//    var charge:String,
//    var waitingTime:String,
//    var status:Int)