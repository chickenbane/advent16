package advent16

import junit.framework.Assert.assertEquals
import org.junit.Test

class Day12Test {

    @Test
    fun answer() {
        val answer = Day12.answer()
        assertEquals(318007, answer)
    }

    @Test
    fun answer2() {
        val answer = Day12.answer2()
        assertEquals(9227661, answer)
    }
}