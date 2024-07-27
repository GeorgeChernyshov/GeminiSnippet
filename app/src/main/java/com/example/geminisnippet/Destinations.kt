package com.example.geminisnippet

interface Destination {
    val route: String
    val displayTitle: Int
}

object TravelGuide : Destination {
    override val route = "travelGuide"
    override val displayTitle = R.string.travel_guide_title
}

object TravelAdvice : Destination {
    override val route = "travelAdvice"
    override val displayTitle = R.string.travel_advice_title
}