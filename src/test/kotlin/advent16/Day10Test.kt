package advent16

import org.junit.Assert.assertEquals
import org.junit.Test

class Day10Test {
    @Test
    fun answer() {
        val answer = Day10.answer()
        assertEquals("my part1 answer", 141, answer.part1)
        assertEquals("my part2 answer", 1209, answer.part2)
    }
}