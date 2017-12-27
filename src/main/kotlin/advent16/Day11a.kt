package advent16

import advent16.Day11a.Element.CURIUM
import advent16.Day11a.Element.PLUTONIUM
import advent16.Day11a.Element.RUTHENIUM
import advent16.Day11a.Element.STRONTIUM
import advent16.Day11a.Element.THULIUM
import advent16.Day11a.HolderType.GENERATOR
import advent16.Day11a.HolderType.MICROCHIP
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet

object Day11a {
    enum class Element { HYDROGEN, LITHIUM, STRONTIUM, PLUTONIUM, THULIUM, RUTHENIUM, CURIUM }
    enum class HolderType { GENERATOR, MICROCHIP }
    data class Item(val element: Element, val type: HolderType, val floor: Int)

    enum class ElevatorDirection { UP, DOWN }

    data class Move(val direction: ElevatorDirection, val items: Set<Item>)


    // [] -> []
    // [a] -> [[a]]
    // [a, b] -> [[a, b], [a], [b]]
    // [a, b, c] -> [[a, b], [b, c], [a, c], [a], [b], [c] ]
    fun <T> List<T>.pairs(): Set<Set<T>> {
        val sets = LinkedHashSet<Set<T>>()
        forEach { a ->
            sets.add(setOf(a))
            forEach { b ->
                if (a != b) {
                    sets.add(setOf(a, b))
                }
            }
        }
        return sets
    }

    data class State(val elevator: Int, val items: Set<Item>) {
        private fun nextElevator(move: Move): Int = when (move.direction) {
            ElevatorDirection.UP -> elevator + 1
            ElevatorDirection.DOWN -> elevator - 1
        }

        // True if both source and dest floors are valid after the move
        private fun validMove(move: Move): Boolean {
            if (elevator == 1 && move.direction == ElevatorDirection.DOWN) return false
            if (elevator == TOP_FLOOR && move.direction == ElevatorDirection.UP) return false
            require(move.items.isNotEmpty() && move.items.size <= 2)
            require(move.items.all { it in items && it.floor == elevator })

            val endElevator = nextElevator(move)
            val startItems = items.filter { it.floor == elevator && it !in move.items }
            val endItems = items.filter { it.floor == endElevator || it in move.items }

            fun List<Item>.ok(): Boolean {
                val chips = filter { it.type == HolderType.MICROCHIP }.map { it.element }
                val generators = filter { it.type == GENERATOR }.map { it.element }.toSet()
                val (powered, unpowered) = chips.partition { it in generators }
                return powered.isEmpty() || unpowered.isEmpty()
            }

            return startItems.ok() && endItems.ok()
        }

        fun move(move: Move): State {
            //require(validMove(move))

            val endElevator = nextElevator(move)
            val unmovedItems = items.filter { it !in move.items }
            val movedItems = move.items.map { it.copy(floor = endElevator) }
            val items = (movedItems + unmovedItems).toSet()
            return State(endElevator, items)
        }

        fun lowestItem(): Int = items.minBy { it.floor }!!.floor

        fun done() = items.all { it.floor == TOP_FLOOR }

        // Returns possible valid moves for the state
        fun moves(): List<Move> {
            val list = ArrayList<Move>()
            val pairs = items.filter { it.floor == elevator }.pairs()

            ElevatorDirection.values().forEach { dir ->
                pairs.forEach {
                    val m = Move(dir, it)
                    if (validMove(m)) {
                        list.add(m)
                    }
                }
            }
            return list
        }

        companion object {
            private val TOP_FLOOR = 4
        }
    }

    private class FloorMoves(internal val prev: List<Move>, internal val state: State) {
        private val next: Map<Move, State> by lazy {
            val map = LinkedHashMap<Move, State>()
            val moves = state.moves()
            for (m in moves) {
                map[m] = state.move(m)
            }
            map
        }

        fun isFloorDone(): Boolean {
            val lowest = state.lowestItem()
            return next.values.any { it.lowestItem() > lowest }
        }

        fun nextFloorMoves(): List<FloorMoves> {
            val lowest = state.lowestItem()
            val list = ArrayList<FloorMoves>()
            for ((m, s) in next) {
                if (s.lowestItem() > lowest) {
                    val n = FloorMoves(prev + m, s)
                    list.add(n)
                }
            }
            return list
        }

        fun nextMoves(): List<FloorMoves> {
            val lowest = state.lowestItem()
            val list = ArrayList<FloorMoves>(next.size)
            for ((m, s) in next) {
                // prevent going backwards
                if (s.lowestItem() >= lowest) {
                    val n = FloorMoves(prev + m, s)
                    list.add(n)
                }
            }
            return list
        }
    }

    private fun FloorMoves.nextFloor(): List<FloorMoves> {
        var tryMoves: List<FloorMoves> = listOf(this)
        while (true) {
            if (tryMoves.any { it.isFloorDone() }) {
                return tryMoves.flatMap { it.nextFloorMoves() }
            }
            val next = tryMoves.flatMap { it.nextMoves() }
            println("nextFloor: tried=${tryMoves.size} next=${next.size}")
            tryMoves = next
        }
    }

    fun solve(state: State): List<Move> {
        var tryMoves: List<FloorMoves> = listOf(FloorMoves(listOf(), state))
        while (true) {
            val solved = tryMoves.filter { it.state.done() }
            if (solved.isNotEmpty()) {
                return solved.minBy { it.prev.size }!!.prev
            }
            val nextMoves = tryMoves.flatMap { it.nextFloor() }
            println("solve: tried=${tryMoves.size} next=${nextMoves.size}")
            tryMoves = nextMoves
        }
    }

    fun answer(): Int {

        //The first floor contains a strontium generator, a strontium-compatible microchip, a plutonium generator, and a plutonium-compatible microchip.
        //The second floor contains a thulium generator, a ruthenium generator, a ruthenium-compatible microchip, a curium generator, and a curium-compatible microchip.
        //The third floor contains a thulium-compatible microchip.
        //The fourth floor contains nothing relevant.

        val initial = State(1, setOf(
                Item(STRONTIUM, GENERATOR, 1),
                Item(STRONTIUM, MICROCHIP, 1),
                Item(PLUTONIUM, GENERATOR, 1),
                Item(PLUTONIUM, MICROCHIP, 1),
                Item(THULIUM, GENERATOR, 2),
                Item(RUTHENIUM, GENERATOR, 2),
                Item(RUTHENIUM, MICROCHIP, 2),
                Item(CURIUM, GENERATOR, 2),
                Item(CURIUM, MICROCHIP, 2),
                Item(THULIUM, MICROCHIP, 3)
        ))

        val moves = solve(initial)
        println("moves=$moves")
        return moves.size
    }

}
