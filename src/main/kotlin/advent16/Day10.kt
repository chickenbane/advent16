package advent16

object Day10 {
    /*
    --- Day 10: Balance Bots ---

You come upon a factory in which many robots are zooming around handing small microchips to each other.

Upon closer examination, you notice that each bot only proceeds when it has two microchips, and once it does, it gives each one to a different bot or puts it in a marked "output" bin. Sometimes, bots take microchips from "input" bins, too.

Inspecting one of the microchips, it seems like they each contain a single number; the bots must use some logic to decide what to do with each chip. You access the local control computer and download the bots' instructions (your puzzle input).

Some of the instructions specify that a specific-valued microchip should be given to a specific bot; the rest of the instructions indicate what a given bot should do with its lower-value or higher-value chip.

For example, consider the following instructions:

value 5 goes to bot 2
bot 2 gives low to bot 1 and high to bot 0
value 3 goes to bot 1
bot 1 gives low to output 1 and high to bot 0
bot 0 gives low to output 2 and high to output 0
value 2 goes to bot 2
Initially, bot 1 starts with a value-3 chip, and bot 2 starts with a value-2 chip and a value-5 chip.
Because bot 2 has two microchips, it gives its lower one (2) to bot 1 and its higher one (5) to bot 0.
Then, bot 1 has two microchips; it puts the value-2 chip in output 1 and gives the value-3 chip to bot 0.
Finally, bot 0 has two microchips; it puts the 3 in output 2 and the 5 in output 0.
In the end, output bin 0 contains a value-5 microchip, output bin 1 contains a value-2 microchip, and output bin 2 contains a value-3 microchip. In this configuration, bot number 2 is responsible for comparing value-5 microchips with value-2 microchips.

Based on your instructions, what is the number of the bot that is responsible for comparing value-61 microchips with value-17 microchips?

     */

    private val input by lazy { FileUtil.resourceToList("input10.txt") }

    private val valueOpRegex = """^value (\d+) goes to bot (\d+)$""".toRegex()
    private val giveOpRegex = """^bot (\d+) gives low to ([botup]+ \d+) and high to ([botup]+ \d+)$""".toRegex()

    fun botOp(s: String): BotOp {
        return when {
            s.startsWith("value") -> {
                val result = valueOpRegex.matchEntire(s) ?: throw IllegalArgumentException("valueOpRegex doesn't match for s=$s")
                val (value, bot) = result.destructured
                ValueOp(value.toInt(), BotId(bot.toInt()))
            }
            s.startsWith("bot") -> {
                val result = giveOpRegex.matchEntire(s) ?: throw IllegalArgumentException("giveOpRegex doesn't match for s=$s")
                val (bot, low, high) = result.destructured
                GiveOp(BotId(bot.toInt()), target(low), target(high))
            }
            else -> throw IllegalArgumentException("No botOp for s=$s")
        }
    }

    private fun target(s: String): Target {
        val split = s.split(" ")
        require(split.size == 2) { "split not two elements s=$s" }
        val num = split.last().toInt()
        return when {
            s.startsWith("bot") -> BotId(num)
            s.startsWith("output") -> Output(num)
            else -> throw IllegalArgumentException("target without bot or output s=$s")
        }
    }

    fun answer(part2: Boolean = false): Int {
        val botOps = input.map { botOp(it) }

        // setup bots hashmap
        val bots = LinkedHashMap<BotId, Robot>()
        val giveOps = botOps.filterIsInstance<GiveOp>()
        giveOps.forEach {
            bots.putIfAbsent(it.bot, Robot(it.bot, it))
        }

        val valueOps = botOps.filterIsInstance<ValueOp>()
        valueOps.forEach {
            bots.getValue(it.bot).add(it.value)
        }

        var uno = -1
        val outputs = IntArray(3)

        while (bots.values.any { it.ready() }) {
            val readyBots = bots.values.filter { it.ready() }
            readyBots.forEach {
                val giveLow = it.giveOp.low
                val giveHigh = it.giveOp.high
                val (low, high) = it.chips()
                println("readyBot=${it.bot} low=$low -> $giveLow high=$high -> $giveHigh")
                if (giveLow is BotId) {
                    bots.getValue(giveLow).add(low)
                }
                if (giveHigh is BotId) {
                    bots.getValue(giveHigh).add(high)
                }

                if (low == 17 && high == 61) {
                    uno = it.bot.num
                }
                if (giveLow is Output && giveLow.num < 3) {
                    outputs[giveLow.num] = low
                }
                if (giveHigh is Output && giveHigh.num < 3) {
                    outputs[giveHigh.num] = low
                }
            }
        }

        val dos = outputs.reduce { acc, i -> acc * i }

        return if (part2) dos else uno
    }

    /*
    --- Part Two ---

What do you get if you multiply together the values of one chip in each of outputs 0, 1, and 2?

     */
    fun answer2() = answer(true)
}

internal data class Robot(val bot: BotId, val giveOp: GiveOp) {
    private val chips = ArrayList<Int>()
    internal fun add(chip: Int) {
        if (chips.size == 2) throw IllegalStateException("can't add $chip, already have two chips=$chips")
        chips.add(chip)
    }

    fun ready() = chips.size == 2

    fun chips(): Pair<Int, Int> {
        require(chips.size == 2)
        val min = chips.min() ?: throw IllegalStateException("no min?")
        val max = chips.max() ?: throw IllegalStateException("no max?")
        chips.clear()
        return min to max
    }
}

internal sealed class Target
internal data class BotId(val num: Int) : Target()
internal data class Output(val num: Int) : Target()

sealed class BotOp
internal data class ValueOp(val value: Int, val bot: BotId) : BotOp()
internal data class GiveOp(val bot: BotId, val low: Target, val high: Target) : BotOp()