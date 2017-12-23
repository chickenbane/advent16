package advent16

import advent16.Day11a.Element.CURIUM
import advent16.Day11a.Element.PLUTONIUM
import advent16.Day11a.Element.RUTHENIUM
import advent16.Day11a.Element.STRONTIUM
import advent16.Day11a.Element.THULIUM
import advent16.Day11a.HolderType.GENERATOR
import advent16.Day11a.HolderType.MICROCHIP
import java.util.*
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
            require(validMove(move))

            val endElevator = nextElevator(move)
            val unmovedItems = items.filter { it !in move.items }
            val movedItems = move.items.map { it.copy(floor = endElevator) }
            val items = (movedItems + unmovedItems).toSet()
            return State(endElevator, items)
        }

        fun done() = items.all { it.floor == TOP_FLOOR }

        // Returns possible valid moves for the state
        val moves: List<Move> by lazy {
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
            list
        }

        companion object {
            private val TOP_FLOOR = 4
        }
    }

    data class GameMove(val state: State, val move: Move)

    class Game(private val initial: State) {
        val resolved = LinkedHashMap<GameMove, State>()

        fun solve() {
            val moves = initial.moves.map { GameMove(initial, it) }
            val linkedList = LinkedList(moves)
            while (linkedList.isNotEmpty()) {
                println("linked list.size=${linkedList.size} resolved.size=${resolved.size}")
                val gameMove = linkedList.removeFirst()
                if (gameMove !in resolved) {
                    val next = gameMove.state.move(gameMove.move)
                    resolved[gameMove] = next
                    if (!next.done()) {
                        linkedList.addAll(next.moves.map { GameMove(next, it) })
                    }
                }
            }
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

        val game = Game(initial)
        game.solve()
        return 1
    }

}
