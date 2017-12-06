package advent16

object Day03 {

/*
--- Day 3: Squares With Three Sides ---

Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture that makes up this part of Easter Bunny HQ. This must be a graphic design department; the walls are covered in specifications for triangles.

Or are they?

The design document gives the side lengths of each triangle it describes, but... 5 10 25? Some of these aren't triangles. You can't help but mark the impossible ones.

In a valid triangle, the sum of any two sides must be larger than the remaining side. For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.

In your puzzle input, how many of the listed triangles are possible?

*/

    data class Tri(val a: Int, val b: Int, val c: Int) {
        fun isTriangle(): Boolean = (a + b) > c && (a + c) > b && (b + c) > a
    }

    val input: List<String> by lazy { FileUtil.resourceToList("input03.txt") }

    fun answer(): Int {
        val tris = input.map {
            val nums = it.trim().split(Regex("""\s+"""), 3).map { it.toInt() }
            require(nums.size == 3)
            Tri(nums[0], nums[1], nums[2])
        }
        return tris.filter { it.isTriangle() }.count()
    }

/*
--- Part Two ---

Now that you've helpfully marked up their design documents, it occurs to you that triangles are specified in groups of three vertically. Each set of three numbers in a column specifies a triangle. Rows are unrelated.

For example, given the following specification, numbers with the same hundreds digit would be part of the same triangle:

101 301 501
102 302 502
103 303 503
201 401 601
202 402 602
203 403 603
In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?

 */

    data class TriTri(val a: Tri, val b: Tri, val c: Tri) {
        val vertA = Tri(a.a, b.a, c.a)
        val vertB = Tri(a.b, b.b, c.b)
        val vertC = Tri(a.c, b.c, c.c)
        fun validTris(): Int = listOf(vertA, vertB, vertC).filter { it.isTriangle() }.count()
    }

    fun answer2(): Int {
        val tris = input.map {
            val nums = it.trim().split(Regex("""\s+"""), 3).map { it.toInt() }
            require(nums.size == 3)
            Tri(nums[0], nums[1], nums[2])
        }
        val tritris = tris.windowed(3, 3, true) {
            require(it.size == 3)
            TriTri(it[0], it[1], it[2])
        }
        return tritris.map { it.validTris() }.sum()
    }


}