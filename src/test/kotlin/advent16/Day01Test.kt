package advent16

import org.junit.Assert
import org.junit.Test

/**
 * Created by joey on 3/5/17.
 */
class Day01Test {
    @Test
    fun example() {
        // Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
        val ex1 = Day01.applyMoves("R2, L3")
        Assert.assertEquals("5 blocks away", 5, ex1.distance)

        // R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
        val ex2 = Day01.applyMoves("R2, R2, R2")
        Assert.assertEquals("2 blocks away", 2, ex2.distance)

        // R5, L5, R5, R3 leaves you 12 blocks away.
        val ex3 = Day01.applyMoves("R5, L5, R5, R3")
        Assert.assertEquals("12 blocks away", 12, ex3.distance)
    }

    @Test
    fun answer() {
        Assert.assertEquals("287 blocks away", 287, Day01.answer())
    }

    @Test
    fun example2() {
        // For example, if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks away, due East.
        // 0, 0 -> R8 -> 8, 0 (E)
        // 8, 0 -> R4 -> 8, -4 (S)
        // 8, 4 -> R4 -> 4, -4 (W)
        // 4, -4 -> R8 -> 4, 4 (N)
        Assert.assertEquals("4 blocks away", 4, Day01.visitTwice("R8, R4, R4, R8"))

        // R8, R4, R4, R8
    }

    @Test
    fun answer2() {
        // 254 is too high
        Assert.assertEquals("133 blocks away", 133, Day01.answer2())
    }
}