package advent16


/**
 * Created by joey on 3/5/17.
 */
object Day01 {
    private val copypasta = """
--- Day 1: No Time for a Taxicab ---

Santa's sleigh uses a very high-precision clock to guide its movements, and the clock's oscillator is regulated by stars.
Unfortunately, the stars have been stolen... by the Easter Bunny. To save Christmas, Santa needs you to retrieve all fifty stars by December 25th.

Collect stars by solving puzzles. Two puzzles will be made available on each day in the advent calendar;
the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near", unfortunately, is as close as you can get -
the instructions on the Easter Bunny Recruiting Document the Elves intercepted start here, and nobody had time to work them out further.

The Document indicates that you should start at the given coordinates (where you just landed) and face North.
Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees, then walk forward the given number of blocks, ending at a new intersection.

There's no time to follow such ridiculous instructions on foot, though, so you take a moment and work out the destination.
Given that you can only walk on the street grid of the city, how far is the shortest path to the destination?

For example:

Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
R5, L5, R5, R3 leaves you 12 blocks away.
How many blocks away is Easter Bunny HQ?
"""

    enum class Dir { NORTH, EAST, SOUTH, WEST }

    data class Pos(val x: Int, val y: Int, val dir: Dir) {
        fun move(move: Move): Pos {
            return when (dir) {
                Dir.NORTH -> {
                    if (move.movedir == MoveDir.LEFT) west(move.blocks)
                    else east(move.blocks)
                }
                Dir.EAST -> {
                    if (move.movedir == MoveDir.LEFT) north(move.blocks)
                    else south(move.blocks)
                }
                Dir.SOUTH -> {
                    if (move.movedir == MoveDir.LEFT) east(move.blocks)
                    else west(move.blocks)
                }
                Dir.WEST -> {
                    if (move.movedir == MoveDir.LEFT) south(move.blocks)
                    else north(move.blocks)
                }
            }
        }

        // same as move(), but returns all positions visited for part 2
        fun track(move: Move): List<Pos> {
            // splits a Move into a list of 1 block Moves
            val moves = (1..move.blocks).map { Move(move.movedir, 1) }

            // first move -> change direction
            val firstMove = moves.first()
            val secondPos = move(firstMove)

            // can't keep calling move() because it will keep changing direction
            val movesAfterFirst = moves.drop(1)
            var curr = secondPos

            val list = arrayListOf<Pos>()
            list.add(secondPos)
            movesAfterFirst.forEach {
                val next = curr.oneBlock()
                list.add(next)
                curr = next
            }
            return list
        }

        private fun oneBlock(): Pos {
            return when (dir) {
                Dir.EAST -> east(1)
                Dir.WEST -> west(1)
                Dir.NORTH -> north(1)
                Dir.SOUTH -> south(1)
            }
        }

        internal fun toPoint() = Point(x, y)

        private fun east(blocks: Int) = copy(x = x + blocks, dir = Dir.EAST)
        private fun west(blocks: Int) = copy(x = x - blocks, dir = Dir.WEST)
        private fun north(blocks: Int) = copy(y = y + blocks, dir = Dir.NORTH)
        private fun south(blocks: Int) = copy(y = y - blocks, dir = Dir.SOUTH)

        val distance = Math.abs(x) + Math.abs(y)

        companion object {
            val ORIGIN = Pos(0, 0, Dir.NORTH)
        }
    }

    enum class MoveDir { LEFT, RIGHT }

    data class Move(val movedir: MoveDir, val blocks: Int)

    fun parseMove(s: String): Move {
        val trimmed = s.trim()
        when {
            trimmed.startsWith("L") -> return Move(MoveDir.LEFT, trimmed.substring(1).toInt())
            trimmed.startsWith("R") -> return Move(MoveDir.RIGHT, trimmed.substring(1).toInt())
            else -> throw IllegalArgumentException("bad move $s")
        }
    }

    fun parseMoves(s: String): List<Move> = s.split(",").map { parseMove(it) }

    fun applyMoves(moves: List<Move>): Pos {
        var pos = Pos.ORIGIN
        moves.forEach { pos = pos.move(it) }
        return pos
    }

    fun applyMoves(s: String): Pos = applyMoves(parseMoves(s))

    val input by lazy { FileUtil.resourceToString("input01.txt") }

    fun answer(): Int = applyMoves(input).distance

    private val part2 = """
--- Part Two ---

Then, you notice the instructions continue on the back of the Recruiting Document. Easter Bunny HQ is actually at the first location you visit twice.

For example, if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks away, due East.

How many blocks away is the first location you visit twice?
"""

    data class Point(val x: Int, val y: Int)

    fun visitTwice(s: String): Int {
        val moves = parseMoves(s)
        val visited = HashSet<Point>()
        var pos = Pos.ORIGIN
        moves.forEach {
            println("moving $it")
            val tracked = pos.track(it)
            tracked.forEach {
                val p = it.toPoint()
                if (p in visited) {
                    println("visited twice $it")
                    return it.distance
                }
            }
            visited.addAll(tracked.map { it.toPoint() })
            pos = tracked.last()
            println("now at $pos")
        }
        println("visited=$visited")
        throw IllegalStateException("visited nowhere twice")
    }

    fun answer2(): Int {
        return visitTwice(input)
    }

    // hiccups on part 2
    // first, just using Pos.move() repeatedly didn't work because that would repeatedly change direction
    // second, visitTwice() tracked Pos in a set, but Pos included Dir so equal positions with different directions would be unequal
    // so, had to create Point class and put Points in the set

}