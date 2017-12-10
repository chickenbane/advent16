package advent16

import org.junit.Assert.assertEquals
import org.junit.Test

class Day02Test {
    @Test
    fun answer() {
        val answer = Day02.answer()
        assertEquals("my answer", "82958", answer)
    }

    @Test
    fun answer2() {
        val answer = Day02.answer2()
        assertEquals("my answer", "B3DB8", answer)
    }
}