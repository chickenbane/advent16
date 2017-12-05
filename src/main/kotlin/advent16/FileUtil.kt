package advent16

/**
 * Created by joey on 3/5/17.
 */
object FileUtil {

    fun resourceToString(path: String): String {
        val resource = ClassLoader.getSystemResourceAsStream(path) ?: throw IllegalArgumentException("Unable to find $path")
        resource.reader().use { return it.readText() }
    }

    fun resourceToList(path: String): List<String> {
        val resource = ClassLoader.getSystemResourceAsStream(path) ?: throw IllegalArgumentException("Unable to find $path")
        return resource.reader().readLines()
    }
}