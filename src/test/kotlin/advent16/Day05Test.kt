package advent16

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Day05Test {
    @Test
    fun example() {
        val firstIndex = Day05.next5ZeroPrefixHash("abc", 0)
        assertEquals("first Index is 3231929", 3231929, firstIndex.index)
        assertTrue("starts with 5 zeros", firstIndex.md5hash.startsWith("00000"))
        assertEquals("first char is 1", '1', firstIndex.md5hash[5])

        val examplePassword = Day05.password("abc")
        assertEquals("example password", "18f47a30", examplePassword)
    }

    @Test
    fun answer() {
        val answer = Day05.answer()
        assertEquals("answer", "c6697b55", answer)
    }

    @Test
    fun example2() {
        val ex2 = Day05.password2("abc")
        assertEquals("example2 pass", "05ace8e3", ex2)
    }

    @Test
    fun answer2() {
        val answer = Day05.answer2()
        assertEquals("answer", "8c35d1ab", answer)
    }

}