import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
//day 1
fun getFirstDigit(line: String): Int{
    for(c in line) {
        if (c.isDigit()){
            return c.digitToInt()
        }
    }
    return 0
}
fun getLastDigit(line: String): Int{
    return getFirstDigit(line.reversed())
}
fun getCalibrationValue(line: String): Int{
    val firstNumber = getFirstDigit(line)*10
    val lastNumber = getLastDigit(line)
    return firstNumber+lastNumber
}
//day 3
val <E> List<E>.lastX: Int
    get() = this.size - 1

fun Char.isSpecial(c: Char? = null): Boolean =
        if (c != null) this == c else this != ' ' && this != '.' && !isDigit()

fun <E> List<List<E>>.adjacentToCoords(position: Pair<Int, Int>): Set<Pair<Int, Int>> {
    val (x, y) = position
    val validPositions = mutableListOf<Pair<Int, Int>>()
    for (dy in -1..1) {
        for (dx in -1..1) {
            if (dx == 0 && dy == 0) continue
            val newX = x + dx
            val newY = y + dy
            if (newX < 0 || newY < 0) continue
            if (newY >= size || newX >= this[newY].size) continue
            validPositions.add(Pair(newX, newY))
        }
    }
    return validPositions.toSet()
}


fun <E> List<List<E>>.adjacentTo(position: Pair<Int, Int>): Set<E> {
    val coords = adjacentToCoords(position)
    return coords.map { (x, y) -> this[y][x] }.toSet()
}