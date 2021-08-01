package com.example.cupofcoffee.helpers.datentime

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DateConversionTests {


    @Test
    fun `test double to time ago conversion is correct`() {
        val currSecs = System.currentTimeMillis().div(1000)

        assertThat((currSecs - 5).toTimeAgo(currSecs))
            .isEqualTo("5 Secs")
        assertThat((currSecs - (65)).toTimeAgo(currSecs))
            .isEqualTo("1 Mins 5 Secs")
    }
}
