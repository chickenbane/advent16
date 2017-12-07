package advent16

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Day04Test {
    @Test
    fun testSumLetterAlphabetical() {
        val a = Day04.ChecksumLetter('a', 1)
        val b = Day04.ChecksumLetter('b', 1)
        val c = Day04.ChecksumLetter('c', 1)

        val sorted = listOf(b, a, c).sorted()
        assertEquals("same count", listOf(a, b, c), sorted)

        val reversed = listOf(c, a, b).sortedDescending()
        assertEquals("reverse", listOf(c, b, a), reversed)
    }

    @Test
    fun testCountOrder() {
        val a = Day04.ChecksumLetter('a', 51)
        val b = Day04.ChecksumLetter('b', 1)
        val c = Day04.ChecksumLetter('c', 70)
        val d = Day04.ChecksumLetter('d', 95)
        val e = Day04.ChecksumLetter('e', 24)

        val sorted = listOf(a, b, c, d, e).sorted()
        assertEquals("by count", listOf(d, c, a, e, b), sorted)
    }

    @Test
    fun testOverallOrder() {
        val a = Day04.ChecksumLetter('a', 51)
        val b = Day04.ChecksumLetter('b', 24)
        val c = Day04.ChecksumLetter('c', 51)
        val d = Day04.ChecksumLetter('d', 95)
        val e = Day04.ChecksumLetter('e', 24)

        val sorted = listOf(a, b, c, d, e).sorted()
        assertEquals("overall order", listOf(d, a, c, b, e), sorted)
    }

    @Test
    fun testChecksum() {
        // aaaaa-bbb-z-y-x-123[abxyz] is a real room
        val uno = Day04.room("aaaaa-bbb-z-y-x-123[abxyz]")
        assertEquals("name", "aaaaa-bbb-z-y-x", uno.name)
        assertEquals("sector", 123, uno.sector)
        assertEquals("checksum", "abxyz", uno.checksum)
        assertTrue("first is real", uno.isReal())

        // a-b-c-d-e-f-g-h-987[abcde] is a real room
        val dos = Day04.room("a-b-c-d-e-f-g-h-987[abcde]")
        assertEquals("name", "a-b-c-d-e-f-g-h", dos.name)
        assertEquals("sector", 987, dos.sector)
        assertEquals("checksum", "abcde", dos.checksum)
        assertTrue("second is real", dos.isReal())

        // not-a-real-room-404[oarel] is a real room.
        val tres = Day04.room("not-a-real-room-404[oarel]")
        assertTrue("third is real", tres.isReal())

        // totally-real-room-200[decoy] is not.
        val cuatro = Day04.room("totally-real-room-200[decoy]")
        assertFalse("fourth is not real", cuatro.isReal())
    }

    @Test
    fun answer() {
        val answer = Day04.answer()
        assertEquals("my answer", 361724, answer)
    }

    @Test
    fun decrypt() {
        //  qzmt-zixmtkozy-ivhz-343 is very encrypted name.
        val whee = Day04.decrypt("qzmt-zixmtkozy-ivhz", 343)
        assertEquals("decrypt", "very encrypted name", whee)
    }

    @Test
    fun answer2() {
        // Day04.rooms.forEach { println( it.decrypted) }
        val ans = Day04.answer2()
        assertEquals("my answer", 482, ans)
    }
}