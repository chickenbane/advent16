package advent16

import advent16.Day11a.Element.HYDROGEN
import advent16.Day11a.Element.LITHIUM
import advent16.Day11a.ElevatorDirection.DOWN
import advent16.Day11a.ElevatorDirection.UP
import advent16.Day11a.HolderType.GENERATOR
import advent16.Day11a.HolderType.MICROCHIP
import advent16.Day11a.Item
import advent16.Day11a.State
import advent16.Day11a.pairs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

class Day11aTest {

    // [] -> []
    // [a] -> [[a]]
    // [a, b] -> [[a, b]]
    // [a, b, c] -> [[a, b], [b, c], [a, c]]
    @Test
    fun pairs() {
        val emptyList: List<Int> = listOf()
        assertEquals("empty list", setOf<Set<Int>>(), emptyList.pairs())

        val oneElement: List<Int> = listOf(1)
        assertEquals("one element", setOf(setOf(1)), oneElement.pairs())

        val twoElement = listOf(1, 2)
        assertEquals("two elements", setOf(setOf(2, 1), setOf(2), setOf(1)), twoElement.pairs())

        val threeElements = listOf(1, 2, 3)
        assertEquals("three elements", setOf(setOf(3, 1), setOf(3, 2), setOf(1, 2), setOf(1), setOf(2), setOf(3)), threeElements.pairs())
    }

    //The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
    //The second floor contains a hydrogen generator.
    //The third floor contains a lithium generator.
    //The fourth floor contains nothing relevant.

    private val exampleState = State(1, setOf(
            Item(HYDROGEN, MICROCHIP, 1),
            Item(LITHIUM, MICROCHIP, 1),
            Item(HYDROGEN, GENERATOR, 2),
            Item(LITHIUM, GENERATOR, 3)
    ))

    @Test
    fun example() {
        val final = exampleState
                .move(up(Item(HYDROGEN, MICROCHIP, 1)))
                .move(up(Item(HYDROGEN, MICROCHIP, 2), Item(HYDROGEN, GENERATOR, 2)))
                .move(down(Item(HYDROGEN, MICROCHIP, 3)))
                .move(down(Item(HYDROGEN, MICROCHIP, 2)))
                .move(up(Item(HYDROGEN, MICROCHIP, 1), Item(LITHIUM, MICROCHIP, 1)))
                .move(up(Item(HYDROGEN, MICROCHIP, 2), Item(LITHIUM, MICROCHIP, 2)))
                .move(up(Item(HYDROGEN, MICROCHIP, 3), Item(LITHIUM, MICROCHIP, 3)))
                .move(down(Item(HYDROGEN, MICROCHIP, 4)))
                .move(up(Item(HYDROGEN, GENERATOR, 3), Item(LITHIUM, GENERATOR, 3)))
                .move(down(Item(LITHIUM, MICROCHIP, 4)))
                .move(up(Item(HYDROGEN, MICROCHIP, 3), Item(LITHIUM, MICROCHIP, 3)))

        assertEquals("elevator on 4", 4, final.elevator)
        assertTrue("all items on 4", final.items.all { it.floor == 4 })
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Elevator won't go down on floor 1`() {
        exampleState.move(Day11a.Move(DOWN, setOf(Item(HYDROGEN, MICROCHIP, 1))))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Empty elevator won't move`() {
        exampleState.move(Day11a.Move(UP, setOf()))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Move cannot have more than two items`() {
        val allItemsOn1 = setOf(
                Item(HYDROGEN, MICROCHIP, 1),
                Item(LITHIUM, MICROCHIP, 1),
                Item(HYDROGEN, GENERATOR, 1),
                Item(LITHIUM, GENERATOR, 1)
        )
        val start = State(1, allItemsOn1)
        start.move(up(Item(HYDROGEN, MICROCHIP, 1), Item(LITHIUM, MICROCHIP, 1), Item(LITHIUM, GENERATOR, 1)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Powered chip + unpowered chip blows up`() {
        exampleState.move(up(Item(HYDROGEN, MICROCHIP, 1), Item(LITHIUM, MICROCHIP, 1)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Cannot leave a floor with fried chip`() {
        val allItemsOn1 = setOf(
                Item(HYDROGEN, MICROCHIP, 1),
                Item(LITHIUM, MICROCHIP, 1),
                Item(HYDROGEN, GENERATOR, 1),
                Item(LITHIUM, GENERATOR, 1)
        )
        val start = State(1, allItemsOn1)
        start.move(up(Item(LITHIUM, GENERATOR, 1)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Cannot go to a floor with fried chip`() {
        val items = setOf(
                Item(HYDROGEN, MICROCHIP, 2),
                Item(LITHIUM, MICROCHIP, 2),
                Item(HYDROGEN, GENERATOR, 1),
                Item(LITHIUM, GENERATOR, 1)
        )
        val start = State(1, items)
        start.move(up(Item(LITHIUM, GENERATOR, 1)))
    }

    private fun up(vararg items: Item) = Day11a.Move(UP, items.toSet())
    private fun down(vararg items: Item) = Day11a.Move(DOWN, items.toSet())

    @Test
    fun solveExample() {
        val moves = Day11a.solve(exampleState)
        println("example moves=$moves")
        assertEquals("example had 11 moves", 11, moves.size)
    }

    @Test
    fun answer() {
        val answer = Day11a.answer()
        assertEquals("my answer", 12, answer)
    }

}