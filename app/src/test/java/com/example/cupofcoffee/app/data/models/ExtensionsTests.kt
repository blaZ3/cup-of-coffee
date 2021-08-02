package com.example.cupofcoffee.app.data.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ExtensionsTests {
    @Test
    fun `test string as short name is computed correctly`() {
        assertThat("abcd".asShortName()).isEqualTo("abcd")
        assertThat("t_abcd".asShortName()).isEqualTo("abcd")
        assertThat("t_a_bcd".asShortName()).isEqualTo("a_bcd")
    }

}
