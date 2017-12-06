package advent16

import org.junit.Assert.assertEquals
import org.junit.Test

class Day03Test {
    @Test
    fun answer() {
        val answer = Day03.answer()
        assertEquals("my answer", 982, answer)
    }

    @Test
    fun answer2() {
        val answer = Day03.answer2()
        assertEquals("my answer", 1826, answer)
    }
}