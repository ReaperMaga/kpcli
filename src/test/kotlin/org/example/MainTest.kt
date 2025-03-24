package org.example

import kotlin.test.Test
import kotlin.test.assertNotNull

class MainTest {
    @Test
    fun `App has a greeting`() {
        val classUnderTest = App()
        assertNotNull(classUnderTest.greeting, "app should have a greeting")
    }
}
