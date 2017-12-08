package advent16

import org.junit.Assert.assertEquals
import org.junit.Test

class Day06Test {
    private val exampleStrings = """
eedadn
drvtee
eandsr
raavrd
atevrs
tsrnev
sdttsa
rasrtv
nssdts
ntnada
svetve
tesnvt
vntsnd
vrdear
dvrsen
enarar
            """
    private val exampleList = exampleStrings.split("\n").map { it.trim() }.filter { it.isNotBlank() }

    @Test
    fun example() {
        val answer = Day06.frequentLetters(exampleList)
        assertEquals("example", "easter", answer)
    }

    @Test
    fun answer() {
        val answer = Day06.answer()
        assertEquals("my answer", "qrqlznrl", answer)
    }

    @Test
    fun example2() {
        val answer = Day06.frequentLetters(exampleList, false)
        assertEquals("example", "advent", answer)
    }

    @Test
    fun answer2() {
        val answer = Day06.answer2()
        assertEquals("my answer", "kgzdfaon", answer)
    }
}