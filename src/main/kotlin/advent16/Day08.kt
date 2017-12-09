package advent16

object Day08 {
    /*
    --- Day 8: Two-Factor Authentication ---

You come across a door implementing what you can only assume is an implementation of two-factor authentication after a long game of requirements telephone.

To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk). Then, it displays a code on a little screen, and you type that code on a keypad. Then, presumably, the door unlocks.

Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart and figured out how it works. Now you just have to work out what the screen would have displayed.

The magnetic strip on the card you swiped encodes a series of instructions for the screen; these instructions are your puzzle input. The screen is 50 pixels wide and 6 pixels tall, all of which start off, and is capable of three somewhat peculiar operations:

rect AxB turns on all of the pixels in a rectangle at the top-left of the screen which is A wide and B tall.
rotate row y=A by B shifts all of the pixels in row A (0 is the top row) right by B pixels. Pixels that would fall off the right end appear at the left end of the row.
rotate column x=A by B shifts all of the pixels in column A (0 is the left column) down by B pixels. Pixels that would fall off the bottom appear at the top of the column.
For example, here is a simple sequence on a smaller screen:

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
As you can see, this display technology is extremely powerful, and will soon dominate the tiny-code-displaying-screen market. That's what the advertisement on the back of the display tries to convince you, anyway.

There seems to be an intermediate check of the voltage used by the display: after you swipe your card, if the screen did work, how many pixels should be lit?
     */

    private val input: List<String> by lazy { FileUtil.resourceToList("input08.txt") }

    fun op(line: String): ScreenOp {
        return when {
            line.startsWith("rect") -> {
                val (wide, tall) = line.drop(5).split("x").map { it.toInt() }
                Rect(wide, tall)
            }
            line.startsWith("rotate row") -> {
                val (row, pixels) = line.drop(13).split(" by ").map { it.toInt() }
                RotateRow(row, pixels)
            }
            line.startsWith("rotate column") -> {
                val (col, pixels) = line.drop(16).split(" by ").map { it.toInt() }
                RotateCol(col, pixels)
            }
            else -> throw IllegalArgumentException("can't process line=$line")
        }
    }

    fun answer(): Int {
        val screen = Screen(50, 6)
        input.map { op(it) }.forEach { screen.op(it) }
        return screen.numLit()
    }

    /*
    --- Part Two ---

You notice that the screen is only capable of displaying capital letters; in the font it uses, each letter is 5 pixels wide and 6 tall.

After you swipe your card, what code is the screen trying to display?

     */

    fun answer2() {
        val screen = Screen(50, 6)
        input.map { op(it) }.forEach { screen.op(it) }
        println(screen)
    }

}


sealed class ScreenOp
internal data class Rect(val wide: Int, val tall: Int) : ScreenOp()
internal data class RotateRow(val row: Int, val pixels: Int) : ScreenOp()
internal data class RotateCol(val col: Int, val pixels: Int) : ScreenOp()

class Screen(private val width: Int, private val height: Int) {
    // 6 pixels tall (rows, 0 is top row)
    // 50 pixels wide (cols, 0 left column)
    private val pixels = Array(height) { BooleanArray(width) }

    fun op(op: ScreenOp) = when (op) {
        is Rect -> {
            for (row in 0 until op.tall) {
                for (col in 0 until op.wide) {
                    pixels[row][col] = true
                }
            }
        }
        is RotateRow -> {
            val currRow: MutableList<Boolean> = pixels[op.row].toMutableList()

            currRow.rotate(op.pixels)

            for ((col, on) in currRow.withIndex()) {
                pixels[op.row][col] = on
            }
        }
        is RotateCol -> {
            val currCol: MutableList<Boolean> = (0 until height).map { pixels[it][op.col] }.toMutableList()

            currCol.rotate(op.pixels)

            for ((row, on) in currCol.withIndex()) {
                pixels[row][op.col] = on
            }
        }
    }

    // remove last element and insert to front of the list n times
    private fun MutableList<Boolean>.rotate(pixels: Int) {
        (0 until pixels).forEach {
            val last = removeAt(lastIndex)
            add(0, last)
        }
    }

    fun numLit(): Int = pixels.indices.sumBy { row -> pixels[row].count { it } }

    override fun toString(): String = pixels.joinToString("\n") { it.map { if (it) '#' else '.' }.joinToString("") }
}


