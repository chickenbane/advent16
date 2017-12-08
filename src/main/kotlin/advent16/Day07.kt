package advent16

object Day07 {
    /*
    --- Day 7: Internet Protocol Version 7 ---

While snooping around the local network of EBHQ, you compile a list of IP addresses (they're IPv7, of course; IPv6 is much too limited). You'd like to figure out which IPs support TLS (transport-layer snooping).

An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or ABBA. An ABBA is any four-character sequence which consists of a pair of two different characters followed by the reverse of that pair, such as xyyx or abba. However, the IP also must not have an ABBA within any hypernet sequences, which are contained by square brackets.

For example:

abba[mnop]qrst supports TLS (abba outside square brackets).
abcd[bddb]xyyx does not support TLS (bddb is within square brackets, even though xyyx is outside square brackets).
aaaa[qwer]tyui does not support TLS (aaaa is invalid; the interior characters must be different).
ioxxoj[asdfgh]zxcvbn supports TLS (oxxo is outside square brackets, even though it's within a larger string).
How many IPs in your puzzle input support TLS?
     */

    private fun hasAbba(s: String): Boolean {
        val windowed: List<String> = s.windowed(4)
        return windowed.any { it[0] != it[1] && it[0] == it[3] && it[1] == it[2] }
    }

    data class Address(val sequences: List<String>, val bracketed: List<String>) {
        fun supportsTls(): Boolean {
            if (bracketed.any { hasAbba(it) }) return false
            return sequences.any { hasAbba(it) }
        }

        fun supportsSsl(): Boolean {
            val babs = bracketed.flatMap { babs(it) }
            return sequences.any { containsAba(it, babs) }
        }
    }

    private val input: List<String> by lazy { FileUtil.resourceToList("input07.txt") }

    fun address(line: String): Address {
        val sequences = ArrayList<String>()
        val bracketed = ArrayList<String>()
        var sb = StringBuilder()
        for (c in line) {
            when (c) {
                '[' -> {
                    sequences.add(sb.toString())
                    sb = StringBuilder()
                }
                ']' -> {
                    bracketed.add(sb.toString())
                    sb = StringBuilder()
                }
                else -> sb.append(c)
            }
        }
        // input never ends with bracket
        sequences.add(sb.toString())

        return Address(sequences, bracketed)
    }

    fun answer(): Int = input.map { address(it) }.filter { it.supportsTls() }.count()

    /*
    --- Part Two ---

You would also like to know which IPs support SSL (super-secret listening).

An IP supports SSL if it has an Area-Broadcast Accessor, or ABA, anywhere in the supernet sequences (outside any square bracketed sections), and a corresponding Byte Allocation Block, or BAB, anywhere in the hypernet sequences. An ABA is any three-character sequence which consists of the same character twice with a different character between them, such as xyx or aba. A corresponding BAB is the same characters but in reversed positions: yxy and bab, respectively.

For example:

aba[bab]xyz supports SSL (aba outside square brackets with corresponding bab within square brackets).
xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).
aaa[kek]eke supports SSL (eke in supernet with corresponding kek in hypernet; the aaa sequence is not related, because the interior character must be different).
zazbz[bzb]cdb supports SSL (zaz has no corresponding aza, but zbz has a corresponding bzb, even though zaz and zbz overlap).
How many IPs in your puzzle input support SSL?

     */

    private fun babs(s: String): List<String> = s.windowed(3).filter { it[0] == it[2] && it[0] != it[1] }

    private fun containsAba(s: String, babs: List<String>): Boolean {
        val abas = babs(s)
        return abas.any { aba ->
            babs.any { bab -> aba[0] == bab[1] && aba[1] == bab[0] }
        }
    }

    fun answer2(): Int = input.map { address(it) }.filter { it.supportsSsl() }.count()
}