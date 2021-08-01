package com.example.cupofcoffee.helpers.datentime


fun Long.toTimeAgo(comparedTo: Long = System.currentTimeMillis().div(1000)): String? {
    val timeAgoSeconds = comparedTo.minus(this)

    val minutes = timeAgoSeconds / 60
    val secondsLeft = timeAgoSeconds % 60

    val hours = minutes / 60
    val minutesLeft = minutes % 60

    val days = hours / 24
    val hoursLeft = hours % 24

    val months = days / 30 //assuming all months are 30 days
    val daysLeft = days % 30 //assuming all months are 30 days

    val years = months / 12
    val monthsLeft = months % 12

    if (years > 0) {
        return "$years Years $monthsLeft Months $daysLeft Days $hoursLeft Hours " +
                "$minutesLeft Mins $secondsLeft Secs"
    }

    if (months > 0) {
        return "$months Months $daysLeft Days $hoursLeft Hours $minutesLeft Mins $secondsLeft Secs"
    }

    if (days > 0) {
        return "$days Days $hoursLeft Hours $minutesLeft Mins $secondsLeft Secs"
    }

    if (hours > 0) {
        return "$hours Hours $minutesLeft Mins $secondsLeft Secs"
    }
    if (minutes > 0) {
        return "$minutes Mins $secondsLeft Secs"
    }
    if (timeAgoSeconds > 0) {
        return "$timeAgoSeconds Secs"
    }

    return null
}
