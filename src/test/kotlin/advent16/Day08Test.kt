package advent16

import org.junit.Assert.assertEquals
import org.junit.Test

class Day08Test {
    @Test
    fun example() {
        /*
        rect 3x2 creates a small rectangle in the top-left corner:

        ###....
        ###....
        .......
        rotate column x=1 by 1 rotates the second column down by one pixel:

        #.#....
        ###....
        .#.....
        rotate row y=0 by 4 rotates the top row right by four pixels:

        ....#.#
        ###....
        .#.....
        rotate column x=1 by 1 again rotates the second column down by one pixel, causing the bottom pixel to wrap back to the top:

        .#..#.#
        #.#....
        .#.....
        */

        val strOps = """
            rect 3x2
            rotate column x=1 by 1
            rotate row y=0 by 4
            rotate column x=1 by 1
            """
        val ops = strOps.lines().map { it.trim() }.filter { it.isNotBlank() }.map { Day08.op(it) }
        val screen = Screen(7, 3)
        ops.forEach {
            println("\nbefore: $it\n$screen")
            screen.op(it)
            println("\nafter:\n$screen")
        }
        assertEquals("six lit", 6, screen.numLit())
    }

    @Test
    fun answer() {
        val answer = Day08.answer()
        assertEquals("my answer", 115, answer)
    }

    @Test
    fun answer2() {
        println("answer part 2")
        Day08.answer2()
        // EFEYKFRFIJ
    }
}