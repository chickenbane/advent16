package advent16

object Day06 {
    /*
--- Day 6: Signals and Noise ---

Something is jamming your communications with Santa. Fortunately, your signal is only partially jammed, and protocol in situations like this is to switch to a simple repetition code to get the message through.

In this model, the same message is sent repeatedly. You've recorded the repeating message signal (your puzzle input), but the data seems quite corrupted - almost too badly to recover. Almost.

All you need to do is figure out which character is most frequent for each position. For example, suppose you had recorded the following messages:

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
The most common character in the first column is e; in the second, a; in the third, s, and so on. Combining these characters returns the error-corrected message, easter.

Given the recording in your puzzle input, what is the error-corrected version of the message being sent?
     */

    private val input: List<String> by lazy { FileUtil.resourceToList("input06.txt") }

    // similar to day 4, comparable implementation puts greater count before smaller count
    data class CharCount(val char: Char, val count: Int) : Comparable<CharCount> {
        override fun compareTo(other: CharCount): Int = other.count - count
    }

    fun frequentLetters(list: List<String>, mostFrequest: Boolean = true): String {
        val len = list.first().length
        require(list.all { it.length == len })
        val sb = StringBuilder()
        for (i in 0 until len) {
            val c = frequentIndex(list, i, mostFrequest)
            sb.append(c)
        }
        return sb.toString()
    }

    private fun frequentIndex(list: List<String>, index: Int, mostFrequent: Boolean = true): Char {
        val map = LinkedHashMap<Char, Int>()
        list.forEach {
            val char = it[index]
            val count = map[char] ?: 0
            map[char] = count + 1
        }
        val charCounts = map.map { (char, count) -> CharCount(char, count) }
        val counts = if (mostFrequent) charCounts.sorted() else charCounts.sorted().reversed()
        return counts.first().char
    }

    fun answer() = frequentLetters(input)

    /*
--- Part Two ---

Of course, that would be the message - if you hadn't agreed to use a modified repetition code instead.

In this modified code, the sender instead transmits what looks like random data, but for each character, the character they actually want to send is slightly less likely than the others. Even after signal-jamming noise, you can look at the letter distributions in each column and choose the least common letter to reconstruct the original message.

In the above example, the least common character in the first column is a; in the second, d, and so on. Repeating this process for the remaining characters produces the original message, advent.

Given the recording in your puzzle input and this new decoding methodology, what is the original message that Santa is trying to send?
     */

    fun answer2() = frequentLetters(input, false)

}