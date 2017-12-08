package advent16

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Day07Test {
    @Test
    fun example() {
        // abba[mnop]qrst supports TLS (abba outside square brackets).
        val ex1 = Day07.address("abba[mnop]qrst")
        assertTrue("ex1 yes", ex1.supportsTls())

        // abcd[bddb]xyyx does not support TLS (bddb is within square brackets, even though xyyx is outside square brackets).
        val ex2 = Day07.address("abcd[bddb]xyyx")
        assertFalse("ex2 no", ex2.supportsTls())

        // aaaa[qwer]tyui does not support TLS (aaaa is invalid; the interior characters must be different).
        val ex3 = Day07.address("aaaa[qwer]tyui")
        assertFalse("ex3 no", ex3.supportsTls())

        // ioxxoj[asdfgh]zxcvbn supports TLS
        val ex4 = Day07.address("ioxxoj[asdfgh]zxcvbn")
        assertTrue("ex4 yes", ex4.supportsTls())
    }

    @Test
    fun answer() {
        val answer = Day07.answer()
        assertEquals("my answer", 110, answer)
    }

    @Test
    fun example2() {
        // aba[bab]xyz supports SSL (aba outside square brackets with corresponding bab within square brackets).
        val ex1 = Day07.address("aba[bab]xyz")
        assertTrue("ex1 yes", ex1.supportsSsl())

        // xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).
        val ex2 = Day07.address("xyx[xyx]xyx")
        assertFalse("ex2 no", ex2.supportsSsl())

        // aaa[kek]eke supports SSL (eke in supernet with corresponding kek in hypernet; the aaa sequence is not related, because the interior character must be different).
        val ex3 = Day07.address("aaa[kek]eke")
        assertTrue("ex3 yes", ex3.supportsSsl())

        // zazbz[bzb]cdb supports SSL
        val ex4 = Day07.address("zazbz[bzb]cdb")
        assertTrue("ex4 yes", ex4.supportsSsl())
    }

    @Test
    fun answer2() {
        val answer = Day07.answer2()
        assertEquals("my answer", 242, answer)
    }
}