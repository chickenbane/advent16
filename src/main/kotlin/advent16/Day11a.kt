package advent16

import java.util.*
import kotlin.collections.LinkedHashSet

object Day11a {
    enum class Element { HYDROGEN, LITHIUM }
    enum class HolderType { GENERATOR, MICROCHIP }
    data class Item(val element: Element, val type: HolderType, val floor: Int)

    data class State(val elevator: Int, val items: Set<Item>)

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

    // Returns possible valid moves for the state
    fun State.moves(): List<Move> {
        val list = LinkedList<Move>()
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

    // True if both source and dest floors are valid after the move
    fun State.validMove(move: Move): Boolean {
        if (elevator == 1 && move.direction == ElevatorDirection.DOWN) return false
        if (elevator == 4 && move.direction == ElevatorDirection.UP) return false
        require(move.items.isNotEmpty() && move.items.all {it in items && it.floor == elevator})

        val endElevator = when (move.direction) {
            ElevatorDirection.UP -> elevator + 1
            ElevatorDirection.DOWN -> elevator - 1
        }

        val startItems = items.filter { it.floor == elevator && it !in move.items }
        val endItems = items.filter { it.floor == endElevator || it in move.items }

        fun List<Item>.ok(): Boolean {
            val chipElements = filter { it.type == HolderType.MICROCHIP}.map { it.element }
            val generators = filter { it.type == HolderType.GENERATOR }
            val (powered, unpowered) = chipElements.partition { generators.any { g -> g.element == it } }
            return powered.isEmpty() || unpowered.isEmpty()
        }

        //println("startItems=$startItems.ok()=${startItems.ok()} + endItems=$endItems.ok()=${endItems.ok()}")

        return startItems.ok() && endItems.ok()
    }

    fun State.move(move: Move): State {
        require(validMove(move))

        val endElevator = when (move.direction) {
            ElevatorDirection.UP -> elevator + 1
            ElevatorDirection.DOWN -> elevator - 1
        }

        val (_, unmovedItems) = items.partition { it in move.items }
        val movedItems = move.items.map { it.copy(floor = endElevator)}
        val items = (movedItems + unmovedItems).toSet()
        return State(endElevator, items)
    }
}