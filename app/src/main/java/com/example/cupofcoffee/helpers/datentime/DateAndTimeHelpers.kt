package com.example.cupofcoffee.helpers.datentime


fun Long.toTimeAgo(comparedTo: Long = System.currentTimeMillis().div(1000)): String? {
    val timeAgoSeconds = comparedTo.minus(this)

    if (timeAgoSeconds < 0) return null

    val minutes = timeAgoSeconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val months = days / 30 //assuming all months are 30 days
    val years = months / 12

    if (years > 0) {
        return if (years > 1) "$years Years" else "$years Year"
    }

    if (months > 0) {
        return if (months > 1) "$months Months" else "$months Month"
    }

    if (days > 0) {
        return if (days > 1) "$days Days" else "$days Day"
    }

    if (hours > 0) {
        return if (hours > 1) "$hours Hours" else "$hours Hour"
    }
    if (minutes > 0) {
        return if (minutes > 1) "$minutes Mins" else "$minutes Min"
    }

    if (timeAgoSeconds > 0) {
        return if (timeAgoSeconds > 1) "$timeAgoSeconds Secs" else "$timeAgoSeconds Sec"
    }

    return null
}
