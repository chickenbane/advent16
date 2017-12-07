package advent16

object Day04 {
    /*
--- Day 4: Security Through Obscurity ---

Finally, you come across an information kiosk with a list of rooms. Of course, the list is encrypted and full of decoy data, but the instructions to decode the list are barely hidden nearby. Better remove the decoy data first.

Each room consists of an encrypted name (lowercase letters separated by dashes) followed by a dash, a sector ID, and a checksum in square brackets.

A room is real (not a decoy) if the checksum is the five most common letters in the encrypted name, in order, with ties broken by alphabetization. For example:

aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters are a (5), b (3), and then a tie between x, y, and z, which are listed alphabetically.
a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters are all tied (1 of each), the first five are listed alphabetically.
not-a-real-room-404[oarel] is a real room.
totally-real-room-200[decoy] is not.
Of the real rooms from the list above, the sum of their sector IDs is 1514.

What is the sum of the sector IDs of the real rooms?
     */

    val input: List<String> by lazy { FileUtil.resourceToList("input04.txt") }

    data class ChecksumLetter(val letter: Char, val count: Int) : Comparable<ChecksumLetter> {
        override fun compareTo(other: ChecksumLetter): Int {
            if (count != other.count) return other.count - count
            return Character.compare(letter, other.letter)
        }
    }

    fun check(name: String): String {
        val map = LinkedHashMap<Char, Int>()
        name.forEach {
            if (it != '-') {
                val count = map[it] ?: 0
                map[it] = count + 1
            }
        }
        val letters = map.map { (c, i) -> ChecksumLetter(c, i) }.sorted()
        val sb = StringBuilder()
        letters.forEach {
            sb.append(it.letter)
        }
        return sb.toString().take(5)
    }

    data class Room(val name: String, val sector: Int, val checksum: String) {
        fun isReal(): Boolean = check(name) == checksum
        val decrypted: String by lazy { decrypt(name, sector) }
    }

    val roomRegex = """^([a-z-]+)-(\d+)\[([a-z]+)\]$""".toRegex()

    fun room(line: String): Room {
        val matchResult = roomRegex.matchEntire(line) ?: throw IllegalArgumentException("regex doesn't match $line")
        val (name, sector, sum) = matchResult.destructured
        return Room(name, sector.toInt(), sum)
    }

    val rooms = input.map { room(it) }

    fun answer(): Int = rooms.filter { it.isReal() }.sumBy { it.sector }

    /*
--- Part Two ---

With all the decoy data out of the way, it's time to decrypt this list and get moving.

The room names are encrypted by a state-of-the-art shift cipher, which is nearly unbreakable without the right software. However, the information kiosk designers at Easter Bunny HQ were not expecting to deal with a master cryptographer like yourself.

To decrypt a room name, rotate each letter forward through the alphabet a number of times equal to the room's sector ID. A becomes B, B becomes C, Z becomes A, and so on. Dashes become spaces.

For example, the real name for qzmt-zixmtkozy-ivhz-343 is very encrypted name.

What is the sector ID of the room where North Pole objects are stored?
     */

    fun decrypt(encrypted: String, sector: Int): String {
        val rotate = sector % 26
        val sb = StringBuilder()
        encrypted.forEach {
            if (it == '-') {
                sb.append(' ')
            } else {
                sb.append(rot(it, rotate))
            }
        }
        return sb.toString()
    }

    fun rot(c: Char, n: Int): Char {
        val r = c + n
        val wrap = r - 'z'
        if (wrap > 0) return 'a' + (wrap - 1)
        return r
    }

    fun answer2(): Int = rooms.first { it.decrypted.startsWith("North", ignoreCase = true) }.sector

}