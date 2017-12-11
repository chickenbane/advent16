package advent16

import org.junit.Assert.assertEquals
import org.junit.Test

class Day10Test {
    @Test
    fun answer() {
        val answer = Day10.answer()
        assertEquals("my answer", 141, answer)
    }

    @Test
    fun answer2() {
        val answer = Day10.answer2()
        assertEquals("my answer", 1209, answer)
    }
}