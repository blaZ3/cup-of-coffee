package com.example.cupofcoffee.app.data.models

import asShortName
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import cleanSubRedditName

class ExtensionsTests {

    @Test
    fun `test string as short name is computed correctly`() {
        assertThat("abcd".asShortName()).isEqualTo("abcd")
        assertThat("t_abcd".asShortName()).isEqualTo("abcd")
        assertThat("t_a_bcd".asShortName()).isEqualTo("a_bcd")
    }


    @Test
    fun `test that sanitized sub reddit names work`(){
        assertThat("r/news".cleanSubRedditName()).isEqualTo("news")
        assertThat("random".cleanSubRedditName()).isEqualTo("random")
    }

}
