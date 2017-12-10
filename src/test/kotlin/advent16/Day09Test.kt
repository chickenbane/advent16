package advent16

import org.junit.Assert.assertEquals
import org.junit.Test

class Day09Test {
    @Test
    fun example() {
        //ADVENT contains no markers and decompresses to itself with no changes, resulting in a decompressed length of 6.
        val ex1 = Day09.decompress("ADVENT")
        assertEquals("ex1", "ADVENT", ex1)

        //A(1x5)BC repeats only the B a total of 5 times, becoming ABBBBBC for a decompressed length of 7.
        val ex2 = Day09.decompress("A(1x5)BC")
        assertEquals("ex2", "ABBBBBC", ex2)

        //(3x3)XYZ becomes XYZXYZXYZ for a decompressed length of 9.
        assertEquals("ex3", "XYZXYZXYZ", Day09.decompress("(3x3)XYZ"))

        //A(2x2)BCD(2x2)EFG doubles the BC and EF, becoming ABCBCDEFEFG for a decompressed length of 11.
        assertEquals("ex4", "ABCBCDEFEFG", Day09.decompress("A(2x2)BCD(2x2)EFG"))

        //(6x1)(1x3)A simply becomes (1x3)A
        assertEquals("ex5", "(1x3)A", Day09.decompress("(6x1)(1x3)A"))

        //X(8x2)(3x3)ABCY becomes X(3x3)ABC(3x3)ABCY (for a decompressed length of 18), because the decompressed data from the (8x2) marker (the (3x3)ABC) is skipped and not processed further.
        assertEquals("ex6", "X(3x3)ABC(3x3)ABCY", Day09.decompress("X(8x2)(3x3)ABCY"))
    }

    @Test
    fun answer() {
        val answer = Day09.answer()
        assertEquals("my answer", 110346, answer)
    }

    @Test
    fun example2() {
        //(3x3)XYZ still becomes XYZXYZXYZ, as the decompressed section contains no markers.
        assertEquals("ex1", "XYZXYZXYZ".length.toLong(), Day09.size2("(3x3)XYZ"))

        //X(8x2)(3x3)ABCY becomes XABCABCABCABCABCABCY
        assertEquals("ex2", "XABCABCABCABCABCABCY".length.toLong(), Day09.size2("X(8x2)(3x3)ABCY"))

        //(27x12)(20x12)(13x14)(7x10)(1x12)A decompresses into a string of A repeated 241920 times.
        assertEquals("ex3", 241920L, Day09.size2("(27x12)(20x12)(13x14)(7x10)(1x12)A"))

        //(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN becomes 445 characters long.
        assertEquals("ex4", 445L, Day09.size2("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"))
    }

    @Test
    fun answer2() {
        val answer = Day09.answer2()
        assertEquals("my answer", 10774309173, answer)
    }
}